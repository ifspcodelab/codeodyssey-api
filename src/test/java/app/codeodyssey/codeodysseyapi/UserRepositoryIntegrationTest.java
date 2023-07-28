package app.codeodyssey.codeodysseyapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the UserRepository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class UserRepositoryIntegrationTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("check if a email exists and return true")
    void existsByEmail_givenExistingEmail_returnsTrue() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("sergio@example.com");

        assertTrue(exists);
    }

    @Test
    @DisplayName("check if a email does not exist and return false")
    void existsByEmail_givenNonExistingEmail_returnsFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    @DisplayName("get an existing email and return its associated user")
    void getUserByEmail_givenExistingEmail_returnsUser() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        User foundUser = userRepository.getUserByEmail("sergio@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getToken(), foundUser.getToken());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getRole(), foundUser.getRole());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.isValidated(), foundUser.isValidated());
    }

    @Test
    @DisplayName("get a nonexistent email and return null")
    void getUserByEmail_givenNonExistingEmail_returnsNull() {
        User foundUser = userRepository.getUserByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    @DisplayName("get an existing token and return its associated user")
    void getUserByToken_givenExistingToken_returnsUser() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        User foundUser = userRepository.getUserByToken(user.getToken()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getToken(), foundUser.getToken());
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getName(), foundUser.getName());
        assertEquals(user.getRole(), foundUser.getRole());
        assertEquals(user.getPassword(), foundUser.getPassword());
        assertEquals(user.isValidated(), foundUser.isValidated());
    }

    @Test
    @DisplayName("get a nonexistent token and return an optional empty")
    void getUserByToken_givenNonExistingToken_returnsEmptyOptional() {
        Optional<User> foundUser =
                userRepository.getUserByToken(UUID.randomUUID().toString());

        assertTrue(foundUser.isEmpty());
    }

    @Test
    @DisplayName("get validated users and return them in a list")
    void findByIsValidated_givenValidatedUsers_returnsValidatedUsers() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        user1.setValidated(true);
        user2.setValidated(true);
        userRepository.saveAll(List.of(user1, user2));

        List<User> validatedUsers = userRepository.findByIsValidated(true);

        assertThat(validatedUsers).hasSize(validatedUsers.size());
        assertThat(validatedUsers).extracting(User::isValidated).containsOnly(true);
    }

    @Test
    @DisplayName("get non-validated users and return an empty list")
    void findByIsValidated_givenNotValidatedUsers_returnsEmptyList() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        userRepository.saveAll(List.of(user1, user2));

        List<User> validatedUsers = userRepository.findByIsValidated(true);

        assertThat(validatedUsers).isEmpty();
    }
}
