package app.codeodyssey.codeodysseyapi.user.service;

import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SendEmailService {
    private final JavaMailSender mailSender;

    public void sendConfirmationEmail(String recipientEmail, String confirmationLink) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipientEmail);
        message.setSubject("Email Confirmation");

        message.setText("Please, click the link bellow to confirm your registration:\n" + confirmationLink);

        mailSender.send(message);
    }
}
