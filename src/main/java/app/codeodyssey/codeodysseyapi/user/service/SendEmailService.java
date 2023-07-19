package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.SendEmailException;
import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
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

        try {
            mailSender.send(message);

        } catch (MailException ex) {
            System.err.println("Error sending confirmation email to: " + recipientEmail);
            ex.printStackTrace();

            throw new SendEmailException("Error sending confirmation email", ex);
        }
    }
}
