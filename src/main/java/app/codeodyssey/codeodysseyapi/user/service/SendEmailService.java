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

        String emailContent = "<html><body>" +
                "<p>Please click the link below to confirm your email:</p>" +
                "<a href=\"" + confirmationLink + "\">Click here to confirm your email</a>" +
                "</body></html>";

        message.setText(emailContent);

        mailSender.send(message);
    }
}
