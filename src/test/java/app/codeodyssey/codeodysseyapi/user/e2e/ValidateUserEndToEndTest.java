package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("test for the ValidateUserEndpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ValidateUserEndToEndTest {
    @Autowired
    private UserRepository userRepository;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    private RestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("patch a valid user to validate and return UserResponse")
    void patch_givenValidUser_returnsUserResponse() {
        User user = new User("sergio@example.com", "sergio", "password");
        userRepository.save(user);

        Assertions.assertFalse(user.isValidated());

        HttpEntity<User> request = new HttpEntity<>(user);

        UserResponse response = restTemplate.patchForObject("http://localhost:" + port + "/api/v1/users/confirmation/" +
                user.getToken(), request, UserResponse.class);

        Assertions.assertNotNull(request.getBody());
        Assertions.assertNotNull(response);
        Assertions.assertEquals(request.getBody().getEmail(), response.email());
        Assertions.assertEquals(request.getBody().getName(), response.name());
        Assertions.assertEquals(request.getBody().getRole(), response.role());
        Assertions.assertEquals(request.getBody().getId(), response.id());

        Optional<User> foundUserOptional = userRepository.findByEmail(response.email());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertEquals(foundUser.getId(), response.id());
        Assertions.assertEquals(foundUser.getRole(), response.role());
        Assertions.assertEquals(foundUser.getName(), response.name());
        Assertions.assertEquals(foundUser.getEmail(), response.email());
        Assertions.assertEquals(request.getBody().getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("try to patch a user with an expired token and throw exception")
    void patch_givenUserWithExpiredToken_exceptionThrown() {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        HttpEntity<User> request = new HttpEntity<>(user);

        Assertions.assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.patchForObject("http://localhost:" + port + "/api/v1/users/confirmation/" +
                        user.getToken(), request, UserResponse.class),
                "should throw TokenException (Token Expired)"
        );

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertFalse(foundUser.isValidated());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("try to patch a user with a non-existent token and throw an exception")
    void patch_givenNonexistentToken_exceptionThrown() {
        Assertions.assertThrows(
                HttpClientErrorException.NotFound.class,
                () -> restTemplate.patchForObject("http://localhost:" + port + "/api/v1/users/confirmation/" +
                        UUID.randomUUID(), null, Void.class),
                "should throw TokenException (no user associated with this toke"
        );
    }

    @Test
    @DisplayName("try to validate a user already validated and throw an exception")
    void patch_givenUserAlreadyValidated_exceptionThrown() {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setValidated(true);
        userRepository.save(user);

        HttpEntity<User> request = new HttpEntity<>(user);

        Assertions.assertThrows(
                HttpClientErrorException.Conflict.class,
                () -> restTemplate.patchForObject("http://localhost:" + port + "/api/v1/users/confirmation/" +
        user.getToken(), request, UserResponse.class),
                "should throw UserAlreadyValidatedException"
        );

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
    }
}