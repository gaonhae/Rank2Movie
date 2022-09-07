package code.Config;

import code.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    UserService userService;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/user/login").anonymous() // 이미 로그인 되어있는 사람은 중복 로그인 X
                .antMatchers("/user/signup").anonymous() // 이미 로그인 된 사람이 회원가입 X
                .antMatchers("/**").permitAll() // 나머지는 전부 참조 가능
                .and()
                .formLogin()                                    // 로그인 페이지 커스텀
                .loginPage("/user/login")                       // 로그인 페이지 지정
                .loginProcessingUrl("/user/loginprocess")       // 여기로 아이디/패스워드를 POST로 보내준다.
                .usernameParameter("id")                        // 프론트에서 보내주는 아이디의 파라미터
                .passwordParameter("password")                  // 프론트에서 보내주는 패스워드의 파라미터
                .defaultSuccessUrl("/")                         // 로그인 성공하면 이동하는 URL
                .failureUrl("/user/login")    // 로그인 실패하면 이동하는 URL
                .and()
                .logout()                                                               // 로그아웃 설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout")) // 로그아웃할 URL
                .logoutSuccessUrl("/")                                                  // 로그아웃 성공하면 갈 URL
                .invalidateHttpSession(true)                                            // 로그아웃 성공 시 세션 초기화
                .and()
                .csrf()                                                                 // 인터넷 보안 정책 (잠시 무력화시켰음)
                .ignoringAntMatchers("/**")
                .and()
                .userDetailsService(userService); // 로그인 서비스에 이용되는 userDetailService를 상속받은 객체

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
