package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import com.icegreen.greenmail.spring.GreenMailBean;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@SpringBootTest
@DisplayName("test for the SendEmailService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@RunWith(SpringJUnit4ClassRunner.class)
@Testcontainers
public class SendEmailServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @MockBean
    private JavaMailSender mailSender;
    //private GreenMailBean greenMailBean;

//    @BeforeEach
//    void setUp() {
//        greenMailBean = new GreenMailBean();
//    }

    @Test
    @DisplayName("send email to the provided email")
    void sendEmail_givenEmail_success() {
        GreenMail greenMail = new GreenMail(ServerSetup.ALL);
        greenMail.start();

        User user = new User("sergio@example.com", "Sergio","password#123");
        userRepository.save(user);

        sendEmailService.sendEmail(user.getEmail(), user.getToken());

        Assertions.assertTrue(greenMail.waitForIncomingEmail(5000, 1));

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        verify(mailSender).send(any(SimpleMailMessage.class));
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getName(), foundUser.getName());

        greenMail.stop();
    }
}