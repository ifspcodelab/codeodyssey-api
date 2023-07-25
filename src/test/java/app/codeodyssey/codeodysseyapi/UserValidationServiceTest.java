package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    void testValidateUser_ValidToken_UserNotValidated_Success() {
        User user = new User("sergio@example.com", "Sergio", "password");

        userRepository.save(user);

        User validatedUser = userValidationService.validateUser(user.getToken());

        assertNotNull(validatedUser);
        assertTrue(validatedUser.isValidated());
        assertEquals(user.getId(), validatedUser.getId());
    }

    @Test
    void testValidateUser_ValidToken_UserAlreadyValidated_ExceptionThrown() {
        User user = new User("sergio@example.com", "Sergio", "password");
        user.setValidated(true);
        userRepository.save(user);

        assertThrows(UserAlreadyValidatedException.class, () -> userValidationService.validateUser(user.getToken()));
    }

    @Test
    void testValidateUser_ExpiredToken_ExceptionThrown() {
        User user = new User("sergio@example.com", "Sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        assertThrows(TokenException.class, () -> userValidationService.validateUser(user.getToken()));
    }

    @Test
    void testValidateUser_NonExistentToken_ExceptionThrown() {
        assertThrows(TokenException.class, () -> userValidationService.validateUser(UUID.randomUUID().toString()));
    }
}
