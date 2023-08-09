package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("test for the CreateUserEndpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CreateUserEndToEndTest {
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    JavaMailSender mailSender;

    @LocalServerPort
    Integer port;

    String url;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        url = "http://localhost:%d/api/v1/users".formatted(port);
        restTemplate = new RestTemplate();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("post a valid user")
    void post_givenValidUser_returnsUser() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123"));

        UserResponse response = restTemplate.postForObject(url, request, UserResponse.class);

        Assertions.assertNotNull(request.getBody());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getBody().email(), response.email());
        Assertions.assertEquals(request.getBody().name(), response.name());

        Optional<User> foundUser = userRepository.findByEmail(response.email());

        Assertions.assertTrue(foundUser.isPresent());
        Assertions.assertTrue(passwordEncoder.matches(request.getBody().password(), foundUser.get().getPassword()));
        Assertions.assertEquals(response.id(), foundUser.get().getId());
        Assertions.assertEquals(response.role(), foundUser.get().getRole());
        Assertions.assertFalse(foundUser.get().isValidated());
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    @DisplayName("throws exception due the attempt to register a registered email")
    void post_givenUserAlreadyExists_exceptionThrown() {
        User existingUser = new User("sergio@example.com", "Sergio",
                "Password#123");
        userRepository.save(existingUser);

        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.Conflict.class, () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw ViolationException"
        );

        Optional<User> foundUserOptional = userRepository.findByEmail(existingUser.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertEquals(existingUser.getId(), foundUser.getId());
        Assertions.assertEquals(existingUser.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(existingUser.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(existingUser.getToken(), foundUser.getToken());
        Assertions.assertEquals(existingUser.getRole(), foundUser.getRole());
        Assertions.assertEquals(existingUser.getName(), foundUser.getName());
        Assertions.assertEquals(existingUser.getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("throws exception due to password pattern")
    void post_givenInvalidPasswordPattern_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Password123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw InvalidPasswordException"
        );
    }

    @Test
    @DisplayName("throws exception due to empty password")
    void post_givenEmptyPassword_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                ""));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw InvalidPasswordException"
        );
    }

    @Test
    @DisplayName("throws exception due to null password")
    void post_givenNullPassword_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com", null));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw InvalidPasswordException"
        );
    }

    @Test
    @DisplayName("throws exception due to password over the max. length")
    void post_givenPasswordOutOfMaxLength_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "LongPasswordThatExceedsTheMaximumAllowedLength@123456789012345678901234567890123456789012345678901234567890"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw InvalidPasswordException"
        );
    }

    @Test
    @DisplayName("throws exception due to short password")
    void post_givenPasswordOutOfMinLength_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio@example.com",
                "Short@1"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw InvalidPasswordException"
        );
    }

    @Test
    @DisplayName("throws exception due to invalid email format")
    void post_givenInvalidEmail_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio", "sergio",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to invalid name size (less than 5 chars")
    void post_givenInvalidMinNameSize_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Se", "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }


    @Test
    @DisplayName("throws exception due to invalid name size (more than 100 chars")
    void post_givenInvalidMaxNameSize_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio Gabriel de Lima Aquino Sergio Gabriel de Lima Aquino Sergio Gabriel de Lima Aquino Sergio Gabriel de Lima Aquino", "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to null name")
    void post_givenNullName_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand(null, "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to empty name")
    void post_givenEmptyName_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("", "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to blank name")
    void post_givenBlankName_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand(" ", "sergio@example.com",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to null email")
    void post_givenNullEmail_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio Gabriel de Lima Aquino", null,
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to empty email")
    void post_givenEmptyEmail_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio Gabriel de Lima Aquino", "",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }

    @Test
    @DisplayName("throws exception due to blank email")
    void post_givenBlankEmail_exceptionThrown() {
        HttpEntity<CreateUserCommand> request = new HttpEntity<>(new CreateUserCommand("Sergio Gabriel de Lima Aquino", " ",
                "Password#123"));

        Assertions.assertThrows(
                HttpClientErrorException.BadRequest.class,
                () -> restTemplate.postForObject(url, request, UserResponse.class),
                "should throw MethodArgumentNotValidException"
        );
    }
}
