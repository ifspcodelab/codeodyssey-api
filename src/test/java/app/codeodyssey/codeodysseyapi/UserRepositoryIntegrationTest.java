package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
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
    void testExistsByEmail_ExistingEmail_ReturnsTrue() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("sergio@example.com");

        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_NonExistingEmail_ReturnsFalse() {
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }

    @Test
    void testGetUserByEmail_ExistingEmail_ReturnsUser() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        User foundUser = userRepository.getUserByEmail("sergio@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals("sergio@example.com", foundUser.getEmail());
    }

    @Test
    void testGetUserByEmail_NonExistingEmail_ReturnsNull() {
        User foundUser = userRepository.getUserByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    void testGetUserByToken_ExistingToken_ReturnsUser() {
        User user = new User("sergio@example.com", "Sergio", "password");
        userRepository.save(user);

        User foundUser = userRepository.getUserByToken(user.getToken()).orElse(null);

        assertNotNull(foundUser);
        assertEquals(user.getId(), foundUser.getId());
        assertEquals(user.getToken(), foundUser.getToken());
    }

    @Test
    void testGetUserByToken_NonExistingToken_ReturnsEmptyOptional() {
        Optional<User> foundUser = userRepository.getUserByToken(UUID.randomUUID().toString());

        assertTrue(foundUser.isEmpty());
    }

    @Test
    void testFindByIsValidated_ValidatedUsers_ReturnsValidatedUsers() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        user1.setValidated(true);
        user2.setValidated(true);
        userRepository.saveAll(List.of(user1, user2));

        List<User> validatedUsers = userRepository.findByIsValidated(true);

        assertThat(validatedUsers).hasSize(2);
        assertThat(validatedUsers).extracting(User::isValidated).containsOnly(true);
    }

    @Test
    void testFindByIsValidated_NotValidatedUsers_ReturnsEmptyList() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        userRepository.saveAll(List.of(user1, user2));

        List<User> validatedUsers = userRepository.findByIsValidated(true);

        assertThat(validatedUsers).isEmpty();
    }
}
