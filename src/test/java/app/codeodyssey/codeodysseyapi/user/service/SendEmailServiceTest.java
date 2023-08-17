package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class SendEmailServiceTest {
    @Autowired
    private UserRepository userRepository;

    private RestTemplate restTemplate;

    @LocalServerPort
    Integer port;

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        restTemplate = new RestTemplate();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Send email to user and verify its delivery")
    void should_send_email_to_user_with_green_mail_extension() throws MessagingException {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123"));

        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:%d/api/v1/users".formatted(port), request, Void.class);

        Assertions.assertEquals(HttpStatusCode.valueOf(201), response.getStatusCode());

        MimeMessage receivedMessage = greenMail.getReceivedMessages()[0];
        Assertions.assertEquals(Objects.requireNonNull(request.getBody()).email(), receivedMessage.getAllRecipients()[0].toString());
    }
}