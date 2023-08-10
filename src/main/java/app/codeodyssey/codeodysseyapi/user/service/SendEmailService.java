package app.codeodyssey.codeodysseyapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmailService {
    @Value("${frontend.url}")
    private String url;

    private final JavaMailSender mailSender;

    public void sendEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email confirmation");

        message.setText("Please click the link bellow to confirm your registration\n" + url + "/confirmation/"
                + token);
        mailSender.send(message);
    }
}
