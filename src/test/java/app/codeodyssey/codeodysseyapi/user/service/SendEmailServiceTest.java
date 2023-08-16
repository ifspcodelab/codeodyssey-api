package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
//@Testcontainers
public class SendEmailServiceTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserRepository userRepository;


//    @Container
//    static GenericContainer greenMailContainer = new GenericContainer<>(DockerImageName.parse("greenmail/standalone:1.6.1"))
//            .waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
//            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=user:admin")
//            .withExposedPorts(3025);
//
//    @DynamicPropertySource
//    static void configureMailHost(DynamicPropertyRegistry registry) {
//        registry.add("spring.mail.host", greenMailContainer::getHost);
//        registry.add("spring.mail.port", greenMailContainer::getFirstMappedPort);
//    }


    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void should_send_email_to_user_with_green_mail_extension() throws MessagingException {
        //HttpEntity<EmailRequest> request = new HttpEntity<>(new EmailRequest("aquino@example.com", UUID.randomUUID().toString()));

        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123"));


//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<Void> response = this.testRestTemplate.postForEntity("/api/v1/users", request, Void.class);

        Assertions.assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        Assertions.assertEquals(request.getBody().email(), receivedMessage.getAllRecipients()[0].toString());
    }

//    @Test
//    @DisplayName("send email to the provided email")
//    void sendEmail_givenEmail_success() {
//        User user = new User("sergio@example.com", "Sergio","password#123");
//        userRepository.save(user);
//
//        sendEmailService.sendEmail(user.getEmail(), user.getToken());
//
//        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());
//
//        Assertions.assertTrue(foundUserOptional.isPresent());
//
//        User foundUser = foundUserOptional.get();
//
//        verify(mailSender).send(any(SimpleMailMessage.class));
//        Assertions.assertEquals(user.getToken(), foundUser.getToken());
//        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
//        Assertions.assertEquals(user.getId(), foundUser.getId());
//        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
//        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
//        Assertions.assertEquals(user.getRole(), foundUser.getRole());
//        Assertions.assertEquals(user.getName(), foundUser.getName());
//    }
}