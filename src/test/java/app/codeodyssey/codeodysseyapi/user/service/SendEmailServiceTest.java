package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;

@SpringBootTest
@DisplayName("test for the SendEmailService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Testcontainers
public class SendEmailServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @MockBean
    private JavaMailSender mailSender;

    @Container
    static GenericContainer greenMailGenericContainer = new GenericContainer<>(DockerImageName.parse("greenmail/standalone:latest"))
            .waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=user:admin")
            .withExposedPorts(3025);

    @DynamicPropertySource
    static void configureMailHost(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", greenMailGenericContainer::getHost);
        registry.add("spring.mail.port", () -> greenMailGenericContainer.getMappedPort(3025));
    }

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("nao.responda425@gmail.com", "fdswibvggzvdebsl"))
            .withPerMethodLifecycle(false);

    @Test
    @DisplayName("send email to the provided email")
    void sendEmail_givenEmail_success() {
        User user = new User("sergio@example.com", "Sergio","password#123");
        userRepository.save(user);

        sendEmailService.sendEmail(user.getEmail(), user.getToken());

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
    }
}