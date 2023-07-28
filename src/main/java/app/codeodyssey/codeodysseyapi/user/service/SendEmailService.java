package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
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
    private final UserRepository userRepository;

    public void sendEmail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();

        User user = userRepository.getUserByEmail(email);

        message.setTo(email);
        message.setSubject("Email confirmation");

        message.setText("Please click the link bellow to confirm your registration\n" + url + "/confirmation/"
                + user.getToken());
        mailSender.send(message);
    }
}
