package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("test for the CreateUserService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
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


//    @Test
//    @DisplayName("create and save a user")
//    void execute_givenValidUser_returnsUserResponse() {
//        CreateUserCommand userCommand = new CreateUserCommand("Sergio", "sergio@example.com",
//                "password#123");
//        UserResponse user = createUserService.execute(userCommand);
//
//        User foundUser = userRepository.getUserByEmail(user.email());
//
//        assertThat(user).isNotNull();
//        assertEquals(user.id(), foundUser.getId());
//        assertEquals(user.role(), foundUser.getRole());
//        assertEquals(user.name(), foundUser.getName());
//        assertEquals(user.email(), foundUser.getEmail());
//    }

    @Test
    @DisplayName("throws exception due the attempt to create a duplicated user")
    void execute_givenDuplicateUser_throwsException() {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "password#123");

        User existingUser = new User("sergio@example.com", "Sergio", "password");

        userRepository.save(existingUser);

        assertThrows(ViolationException.class, () -> createUserService.execute(command));

        User foundUser = userRepository.getUserByEmail(existingUser.getEmail());

        assertThat(foundUser).isNotNull();
        assertEquals(existingUser.getId(), foundUser.getId());
        assertEquals(existingUser.getToken(), foundUser.getToken());
        assertEquals(existingUser.getName(), foundUser.getName());
        assertEquals(existingUser.isValidated(), foundUser.isValidated());
        assertEquals(existingUser.getPassword(), foundUser.getPassword());
        assertEquals(existingUser.getRole(), foundUser.getRole());
    }
}

