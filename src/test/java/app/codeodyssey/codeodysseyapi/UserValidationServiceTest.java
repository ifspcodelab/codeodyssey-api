package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@Testcontainers
public class UserValidationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidationService userValidationService;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    public UserValidationServiceTest() {
    }


    @BeforeEach
    void setUp() {
        reset(userRepository);
    }

    @Test
    void testValidateUser_ValidToken_UserNotValidated_Success() {
        User user = new User("sergio@example.com", "Sergio", "password");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.getUserByToken(user.getToken())).thenReturn(Optional.of(user));

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

        when(userRepository.getUserByToken(user.getToken())).thenReturn(Optional.of(user));

        assertThrows(UserAlreadyValidatedException.class, () -> userValidationService.validateUser(user.getToken()));
    }

    @Test
    void testValidateUser_ExpiredToken_ExceptionThrown() {
        User user = new User("sergio@example.com", "Sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        when(userRepository.getUserByToken(user.getToken())).thenReturn(Optional.of(user));

        assertThrows(TokenException.class, () -> userValidationService.validateUser(user.getToken()));
    }

    @Test
    void testValidateUser_NonExistentToken_ExceptionThrown() {
        when(userRepository.getUserByToken(anyString())).thenReturn(Optional.empty());

        assertThrows(TokenException.class, () -> userValidationService.validateUser(UUID.randomUUID().toString()));
    }
}
