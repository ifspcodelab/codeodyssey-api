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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class SendEmailServiceTest {

    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendEmailService sendEmailService;

    @MockBean
    private JavaMailSender mailSender;

    @LocalServerPort
    Integer port;

    @Container
    static GenericContainer greenMailGenericContainer = new GenericContainer<>(DockerImageName.parse("greenmail/standalone:latest"))
            .waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail.users=user:admin")
            .withExposedPorts(3025);

    @DynamicPropertySource
    static void configureMailHost(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", greenMailGenericContainer::getHost);
        registry.add("spring.mail.port", greenMailGenericContainer::getFirstMappedPort);
    }

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(true);


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        restTemplate = new RestTemplate();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

//    @Test
//    void should_send_email_to_user_with_green_mail_extension() throws JSONException, MessagingException {
//        JSONObject emailJsonObject = new JSONObject();
//        emailJsonObject.put("name", "Sergio");
//        emailJsonObject.put("email", "sergio@example.com");
//        emailJsonObject.put("password", "Senha@01");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<String> emailRequest = new HttpEntity<>(emailJsonObject.toString(), headers);
//
//        ResponseEntity<UserResponse> response = restTemplate.postForEntity("http://localhost:%d/api/v1/users".formatted(port), emailRequest, UserResponse.class);
//
//        assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());
//
//        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
//
//        Assertions.assertEquals(1, receivedMessage.getAllRecipients().length);
//        Assertions.assertEquals("sergio@example", receivedMessage.getAllRecipients()[0].toString());
//        Assertions.assertEquals("Email confirmation", receivedMessage.getSubject());
//    }

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