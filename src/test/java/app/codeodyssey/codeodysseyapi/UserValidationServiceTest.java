package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("test for the UserValidationService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class UserValidationServiceTest {

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private UserValidationService userValidationService;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("validate user who has a valid token and return the user")
    void validateUser_givenValidToken_UserNotValidated_returnUser() {
        User user = new User("sergio@example.com", "Sergio", "password");

        userRepository.save(user);

        User validatedUser = userValidationService.validateUser(user.getToken());

        assertNotNull(validatedUser);
        assertTrue(validatedUser.isValidated());
        assertEquals(user.getId(), validatedUser.getId());
        assertEquals(user.getToken(), validatedUser.getToken());
        assertEquals(user.getEmail(), validatedUser.getEmail());
        assertEquals(user.getName(), validatedUser.getName());
        assertEquals(user.getRole(), validatedUser.getRole());
        assertEquals(user.getPassword(), validatedUser.getPassword());
    }

    @Test
    @DisplayName("try to validate user who is already validated and throw an exception")
    void validateUser_givenValidToken_UserAlreadyValidated_exceptionThrown() {
        User user = new User("sergio@example.com", "Sergio", "password");
        user.setValidated(true);
        userRepository.save(user);

        assertThrows(UserAlreadyValidatedException.class, () -> userValidationService.validateUser(user.getToken()));
        assertTrue(user.isValidated());

        User foundUser = userRepository.getUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertTrue(foundUser.isValidated());
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getToken(), foundUser.getToken());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getRole(), foundUser.getRole());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("try to validate user with an expired token and throw an exception")
    void validateUser_givenExpiredToken_exceptionThrown() {
        User user = new User("sergio@example.com", "Sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        assertThrows(TokenException.class, () -> userValidationService.validateUser(user.getToken()));
        assertFalse(user.isValidated());

        User foundUser = userRepository.getUserByEmail(user.getEmail());

        assertNotNull(foundUser);
        assertFalse(foundUser.isValidated());
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getToken(), foundUser.getToken());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getRole(), foundUser.getRole());
        assertEquals(user.getPassword(), foundUser.getPassword());
    }

    @Test
    @DisplayName("try to validate a user with a non-existent token and throw an exception")
    void validateUser_givenNonExistentToken_exceptionThrown() {
        assertThrows(TokenException.class, () -> userValidationService.validateUser(UUID.randomUUID().toString()));
    }
}
