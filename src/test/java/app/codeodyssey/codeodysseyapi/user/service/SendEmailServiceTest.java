package app.codeodyssey.codeodysseyapi.user.service;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the SendEmailService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class SendEmailServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    @DisplayName("send email to the provided email")
    void sendEmail_givenEmail_success() {
        User user = new User("sergio@example.com", "Sergio", passwordEncoder.encode("password#123"));
        userRepository.save(user);

        sendEmailService.sendEmail(user.getEmail());

        User foundUser = userRepository.getUserByEmail(user.getEmail());

        verify(mailSender).send(any(SimpleMailMessage.class));
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getPassword(), foundUser.getPassword());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getName(), foundUser.getName());
    }
}
