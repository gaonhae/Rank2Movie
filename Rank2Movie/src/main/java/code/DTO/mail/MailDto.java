package code.DTO.mail;

import lombok.*;

@Getter @Setter @Builder
@ToString @EqualsAndHashCode
@RequiredArgsConstructor @AllArgsConstructor
public class MailDto
{
    private String address;
    private String title;
    private String message;
}
