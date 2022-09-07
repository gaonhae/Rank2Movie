package code.Controller;

import code.DTO.mail.MailDto;
import code.DTO.user.SignupDto;
import code.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class MainController
{
    @Autowired
    private EmailService emailService;

    @RequestMapping("/")
    public String home()
    {

        return "/home";
    }

    @PostMapping("/mail")
    @ResponseBody
    public String mailSend(@Param("address") String address)
    {
        MailDto dto = new MailDto();

        dto.setAddress(address);
        dto.setTitle("내가 아니어도 누군가~");
        dto.setMessage("사랑해, 줄 사람 많을거야~");
        emailService.mailSend(dto);
        return "success";
    }

    @RequestMapping("/ranking")
    public String ranking()
    {
        return "ranking";
    }

}
