package code.Service;

import code.DTO.user.UpdateDto;
import code.DTO.user.UserDto;
import code.DTO.user.SignupDto;
import code.Domain.User.UserEntity;
import code.Domain.User.UserRepository;
import code.Domain.User.UserReputationEntity;
import code.Domain.User.UserReputationRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserReputationRepository userReputationRepository;

    //ID 체크에 사용되는 매서드
    public Boolean availableId(String id) {
        UserEntity user = userRepository.findById(id);
        // 쓸 수 있는 아이디라면 true, 아니라면 false
        return user == null;
    }

    // 스프링 시큐리티에서 UserDto를 받아갈때 쓰는 메서드
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        UserEntity user = userRepository.findById(userId); // 일치하는게 없으면 그냥 null이 들어가도 상관 없다.

        return new UserDto(user, Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())));
    }

    // 로그인 확인용 매서드
    public String logincheck(String id, String pw) {
        if (id.equals("")) return "ID를 입력해주세요.";
        if (pw.equals("")) return "비밀번호를 입력해주세요.";

        UserEntity user = userRepository.findById(id);
        if (user == null) return "일치하는 ID가 존재하지 않습니다.";

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(pw, user.getUserPassword()))
            return "비밀번호가 다릅니다.";

        return "success";
    }

    // Read, 유저 상세보기 정보를 보내주는 매서드
    public String userDetail(Long userNo) {
        JSONObject object = new JSONObject();
        UserEntity user = userRepository.findByUserNo(userNo);
        if (user == null) return object.toString(); // 없으면 그냥 반환

        // JSON에 데이터 삽입 + 추가적으로 유저 평판도 넣어줘야 함.
        object.put("userNo", user.getUserNo());
        object.put("userName", user.getUserName());
        object.put("userId", user.getUserId());
        object.put("userDetail", user.getUserDetail());

        // 현재 유저의 번호와 요청 받은 번호가 같다면 true, 아니면 false
        // 옵셔널이라 이렇게 했는데.. 흠,,, 더 좋은 방법이 떠오르면 고쳐보겠다.
        // 해결했다. Optional.map()을 쓰면 훨씬 깔끔하게 코딩할 수 있다!
        Optional<UserDto> dto = nowUser();
        boolean owner = dto.map(u -> u.getNo()==userNo).orElse(false);
        if(owner) object.put("owner", "true");
        else object.put("owner", "false");

        return object.toString();
    }

    // Create, 유저 생성
    @Transactional
    public String createUser(SignupDto user) {

        try {
            userRepository.save(user.toEntity());
        } catch (IllegalAccessError e) {
            // 오류 발생 시, 닉네임 중복 문제
            return "닉네임이 중복되었습니다.";
        }

        return "success";
    }

    // Update, 유저 정보 수정
    public String update(UpdateDto user) {

        try {
            Long no = user.getNo();
            UserEntity entity = userRepository.findByUserNo(no);
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (!encoder.matches(user.getPw(), entity.getUserPassword())) return "비밀번호가 다릅니다.";
            user.updateEntity(entity);
            userRepository.save(entity);

        } catch (IllegalAccessError e) {
            return "사용되고 있는 ID나 닉네임일 수 있습니다.";
        }

        return "success";
    }

    // Delete, 유저 탈퇴 처리
    public String delete(Long no, String pw) {
        UserEntity user = userRepository.findByUserNo(no);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(pw, user.getUserPassword())) return "패스워드가 틀렸습니다!";

        // 종속성이 있는 평가들을 모두 삭제해준다.
        userReputationRepository.deleteAll(user.getReceived());
        userReputationRepository.deleteAll(user.getSended());
        userRepository.delete(user);
        return "success";
    }

    // 현재 로그인된 유저의 정보를 UserDto로 반환, 로그인 상태가 아니라면 null 반환.
    public Optional<UserDto> nowUser() {
        // 1. 인증 객체 호출
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        // 2. 인정 정보 객체 호출
        Object principal = authentication.getPrincipal();
        if (principal.equals("anonymousUser")) return Optional.ofNullable(null);
        else return Optional.of((UserDto) principal);
    }

    //////////////////
    // 유저 평판 서비스
    /////////////////

    public String repute(Long no, int reputation) {

        try {
            UserDto sender = nowUser().orElseThrow(() -> new Exception("평가를 하려면 로그인을 해주세요."));

            if (sender.getNo() == no) throw new Exception("본인의 평가를 할 수는 없습니다.");

            // 받아온 피평가자 번호와 평가자 번호로 평가를 찾아보고, 없다면 새로운 평가를 생성하라.
            UserReputationEntity entity = userReputationRepository.findByReceiverAndSender(no, sender.getNo())
                    .orElseGet(() -> {
                        UserEntity recv = userRepository.findByUserNo(no);
                        UserEntity send = userRepository.findByUserNo(sender.getNo());
                        return new UserReputationEntity(recv, send);
                    });

            // 만약 현재 평가와 수신 받은 평가가 같다면, 그 평가를 0으로 초기화해라.( 취소 )
            // 만약 현재 평가와 수신 받은 평가가 같다면, 그 평가로 바꿔줘라. (변경 )
            // 새로 생성된 평가는 무조건 0이기 때문에 항상 받은 평가가 된다.
            if (entity.getReputation() == reputation) entity.setReputation(0);
            else entity.setReputation(reputation);

            userReputationRepository.save(entity);

            return "success";

        } catch (Exception e) {
            return e.getMessage();
        }

    }

    // 해당 유저에 대한 모든 평판을 받아와서, 유저 평판 JSON 객체로 만들어준다.
    // 좋아요(positive) 싫어요(negative) 총합(total) 본인의추천(yours)
    public JSONObject getReputation(Long recv_no) {
        List<UserReputationEntity> list = userRepository.findByUserNo(recv_no).getReceived();
        JSONObject object = new JSONObject();

        // 좋아요와 싫어요를 각각 카운팅 해준다.
        int positive = 0;
        int negative = 0;
        for (UserReputationEntity e : list) {
            if (e.getReputation() == 1) positive++;
            else if (e.getReputation() == -1) negative++;
        }

        object.put("positive", positive);   // 긍정
        object.put("negative", negative);   // 부정
        object.put("total", positive - negative);   // 총합

        // 현재 로그인한 유저의 평가를 받아온다.
        Long nowUserNo = nowUser().map(UserDto::getNo).orElse(-1L); // 현재 로그인 중인 유저의 번호를 받아오는데, 없으면 말도 안되는 번호인 -1을 받아온다.
        int nowReputation = list.stream()
                .filter(e -> Objects.equals(e.getSender().getUserNo(), nowUserNo)).findAny()
                .map(UserReputationEntity::getReputation)
                .orElse(0); // 리스트에서 로그인된 유저의 평가를 찾아오는데, 없거나/로그인도어있지 않으면 무반응(0)을 반환.

        object.put("yours", nowReputation); // 현재 로그인된 유저의 평가

        return object;
    }

}

