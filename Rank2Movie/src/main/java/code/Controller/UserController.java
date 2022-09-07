package code.Controller;

import code.DTO.user.SignupDto;
import code.DTO.user.UpdateDto;
import code.Service.EmailService;
import code.Service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Convert;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;

    ////////////////////
    // 매핑 파트
    ////////////////////
    @RequestMapping("/signup")
    public String signup() {
        return "/user/signup";
    }

    @RequestMapping("/login")
    public String login() {
        return "/user/login";
    }

    @RequestMapping("/{userNo}")
    public String detail() {
        return "/user/detail";
    }

    //////////////////
    // API
    /////////////////
    // 회원가입 유저 생성
    @PostMapping("/")
    @ResponseBody
    public String signup(@Valid SignupDto newUser, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        // 비밀번호 재입력 검사
        if (!newUser.pwConfirm()) return "비밀번호가 서로 다릅니다.";

        return userService.createUser(newUser);

    }

    // 유효 아이디인지 체크
    @GetMapping("/idcheck")
    @ResponseBody
    public String idCheck(@Param("id") String id) {
        if (id.length() == 0) return "아이디를 입력해주세요.";
        else if (id.length() < 7 || 28 < id.length()) return "아이디는 7자에서 28자여야 합니다.";

        // 성공 시 success, 실패 시 실패 메시지 보냄.
        if (userService.availableId(id)) return "success";
        else return "이미 존재하는 아이디입니다.";
    }

    // 로그인 유효성 검사
    @PostMapping("/logincheck")
    @ResponseBody
    public String loginCheck(@Param("id") String id, @Param("pw") String pw) {
        // 성공하면 success, 실패 시 실패 메시지를 전송
        return userService.logincheck(id, pw);
    }

    // Read, 유저 상세 정보 반환
    @GetMapping("/")
    @ResponseBody
    public void getuserdetail(@Param("userNo") Long userNo, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(userService.userDetail(userNo));
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    //Update, 유저 정보 수정
    @PutMapping("/")
    @ResponseBody
    public String updateUserDetail(@Valid UpdateDto data, BindingResult bindingResult) {
        // 유효성 검사를 해줘야 함.
        if (bindingResult.hasErrors()) {
            return bindingResult.getAllErrors().get(0).getDefaultMessage();
        }
        // 성공 시, "success" 반환, 실패 시 오류 메시지 반환
        return userService.update(data);
    }

    //Delete 삭제
    @DeleteMapping("/")
    @ResponseBody
    public String deleteUser(@Param("no") Long no, @Param("pw") String pw) {
        return userService.delete(no, pw);
    }

    @PostMapping("/repute")
    @ResponseBody
    public String repute(@Param("userNo") Long userNo, @Param("reputation") int reputation) {

        return userService.repute(userNo, reputation);
    }

    @GetMapping("/getreputation")
    public void getReputation(@Param("userNo") Long userNo, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.getWriter().print(userService.getReputation(userNo));
        } catch (Exception e) {
            System.out.println(e);
        }

    }


}
