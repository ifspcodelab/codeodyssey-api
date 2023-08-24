package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.ResendEmailCommand;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("test for the ResendEmailEndpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class ResendEmailEndToEndTest {
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    Integer port;

    String url;

    User user;
    User userWithTimeDelayed;

    @BeforeEach
    void setUp() {
        user = UserFactory.sampleUserStudent();
        userWithTimeDelayed = UserFactory.sampleUserResendEmail();
        userRepository.save(user);
        userRepository.save(userWithTimeDelayed);
        url = "http://localhost:%d/api/v1/users/resend-email".formatted(port);
        restTemplate = new RestTemplate();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

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

    @Test
    @DisplayName("post an email")
    void post_resendEmail_returnsResendException() {
        HttpEntity<ResendEmailCommand> request = new HttpEntity<>(new ResendEmailCommand("holmes@gmail.com"));

        Assertions.assertThrows(
                HttpClientErrorException.UnprocessableEntity.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw ResendEmailException"
        );
    }

    @Test
    @DisplayName("post an email with user with delayed time")
    void post_resendEmail_returnsUser() {
        HttpEntity<ResendEmailCommand> request = new HttpEntity<>(new ResendEmailCommand("poirot@email.com"));

        UserResponse response = restTemplate.postForObject(url, request, UserResponse.class);

        Assertions.assertNotNull(request.getBody());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getBody().email(), response.email());

        Optional<User> foundUser = userRepository.findByEmail(response.email());

        Assertions.assertTrue(foundUser.isPresent());
    }
}
