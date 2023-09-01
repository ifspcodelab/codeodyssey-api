package app.codeodyssey.codeodysseyapi.user.service;

import static org.junit.jupiter.api.Assertions.*;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

@SpringBootTest
@DisplayName("test for the CreateUserService")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
@ExtendWith({OutputCaptureExtension.class})
public class CreateUserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateUserService createUserService;
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
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
        @DisplayName("create and save a user")
        void execute_givenValidUser_returnsUserResponse(CapturedOutput output) {
            CreateUserCommand userCommand = new CreateUserCommand("Sergio", "sergio@example.com",
                    "password#123");
            UserResponse user = createUserService.execute(userCommand);

            Optional<User> foundUserOptional = userRepository.findByEmail(user.email());

            assertTrue(foundUserOptional.isPresent());

            User foundUser = foundUserOptional.get();

            assertEquals(user.id(), foundUser.getId());
            assertEquals(user.role(), foundUser.getRole());
            assertEquals(user.name(), foundUser.getName());
            assertEquals(user.email(), foundUser.getEmail());
            assertTrue(output.toString().contains("User with id " + user.id() + " was registered"));
        }

    @Test
    @DisplayName("throws exception due the attempt to create a duplicated user")
    void execute_givenDuplicateUser_throwsException() {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "password#123");

        User existingUser = new User("sergio@example.com", "Sergio","password#123");
        userRepository.save(existingUser);

        assertThrows(ViolationException.class, () -> createUserService.execute(command));

        Optional<User> foundUserOptional = userRepository.findByEmail(existingUser.getEmail());

        assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        assertEquals(existingUser.getId(), foundUser.getId());
        assertEquals(existingUser.getToken(), foundUser.getToken());
        assertEquals(existingUser.getName(), foundUser.getName());
        assertEquals(existingUser.isValidated(), foundUser.isValidated());
        assertEquals(existingUser.getPassword().trim(), foundUser.getPassword().trim());
        assertEquals(existingUser.getRole(), foundUser.getRole());
    }
}
