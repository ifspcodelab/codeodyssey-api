package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import app.codeodyssey.codeodysseyapi.user.service.SendEmailService;
import app.codeodyssey.codeodysseyapi.user.service.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Testcontainers
public class CreateUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SendEmailService sendEmailService;

    @InjectMocks
    private CreateUserService createUserService;

    @BeforeEach
    void setUp() {
        reset(userRepository, userMapper, passwordEncoder, sendEmailService);
    }

    @Test
    void testExecute_CreateUser_Success() {
        // Arrange
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "password#123");
        String encodedPassword = "encodedPassword#123";

        when(userRepository.existsByEmail(command.email())).thenReturn(false);
        when(passwordEncoder.encode(command.password())).thenReturn(encodedPassword);

        User savedUser = new User(command.email(), command.name(), encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponse expectedResponse = new UserResponse(savedUser.getId(), savedUser.getName(),
                savedUser.getEmail(), savedUser.getRole(), savedUser.getCreatedAt());
        when(userMapper.to(any(User.class))).thenReturn(expectedResponse);

        UserResponse createdUserResponse = createUserService.execute(command);

        verify(userRepository, times(1)).save(any(User.class));
        verify(sendEmailService, times(1)).sendEmail(command.email());

        assertEquals(expectedResponse, createdUserResponse);
    }

    @Test
    void testExecute_CreateUser_UserAlreadyExists() {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "password#123");

        when(userRepository.existsByEmail(command.email())).thenReturn(true);

        assertThrows(ViolationException.class, () -> createUserService.execute(command));
        verify(userRepository, never()).save(any(User.class));
        verify(sendEmailService, never()).sendEmail(anyString());
    }
}