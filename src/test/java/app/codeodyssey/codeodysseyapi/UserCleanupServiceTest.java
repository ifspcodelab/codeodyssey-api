package app.codeodyssey.codeodysseyapi;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserCleanupService;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the UserCleanupService")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class UserCleanupServiceTest {
    @Autowired
    private UserCleanupService userCleanupService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    @DisplayName("clean user who is out the expiration time")
    void cleanup_givenUserOutExpirationTime_returnsNull() {
        User user = new User("Sergio", "sergio@example.com", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));
        userRepository.save(user);

        userCleanupService.cleanupUser();

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNull();
    }

    @Test
    @DisplayName("clean users who are out the expiration time")
    void cleanup_givenUsersOutExpirationTime_returnsEmpty() {
        User user1 = new User("user1@example.com", "User 1", "password");
        user1.setCreatedAt(user1.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        User user2 = new User("user2@example.com", "User 2", "password");
        user2.setCreatedAt(user2.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        User user3 = new User("user3@example.com", "user3", "password");
        user3.setCreatedAt(user3.getCreatedAt().minus(expirationTime, ChronoUnit.SECONDS));

        userRepository.saveAll(List.of(user1, user2, user3));

        userCleanupService.cleanupUser();

        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    @DisplayName("do not clean user who is within the expiration time")
    void cleanup_givenUserWithinExpirationTime_returnsValidUser() {
        User user = new User("sergio@example.com", "Sergio", passwordEncoder.encode("password#123"));
        userRepository.save(user);

        userCleanupService.cleanupUser();

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNotNull();

        User receivedUser = userRepository.getUserByEmail(user.getEmail());

        assertEquals(receivedUser.getEmail(), user.getEmail());
        assertEquals(receivedUser.getId(), user.getId());
        assertEquals(receivedUser.getToken(), user.getToken());
        assertEquals(receivedUser.getPassword(), user.getPassword());
        assertEquals(receivedUser.getRole(), user.getRole());
        assertEquals(receivedUser.isValidated(), user.isValidated());
        assertEquals(receivedUser.getName(), user.getName());
    }

    @Test
    @DisplayName("do not clean users who are within the expiration time")
    void cleanup_givenUsersWithinExpirationTime_returnsValidUsers() {
        User user1 = new User("user1@example.com", "User 1", "password");
        User user2 = new User("user2@example.com", "User 2", "password");
        User user3 = new User("user3@example.com", "user3", "password");

        userRepository.saveAll(List.of(user1, user2, user3));

        userCleanupService.cleanupUser();

        User receivedUser1 = userRepository.getUserByEmail(user1.getEmail());
        User receivedUser2 = userRepository.getUserByEmail(user2.getEmail());
        User receivedUser3 = userRepository.getUserByEmail(user3.getEmail());

        assertEquals(receivedUser1.getEmail(), user1.getEmail());
        assertEquals(receivedUser1.getId(), user1.getId());
        assertEquals(receivedUser1.getToken(), user1.getToken());

        assertEquals(receivedUser2.getEmail(), user2.getEmail());
        assertEquals(receivedUser2.getId(), user2.getId());
        assertEquals(receivedUser2.getToken(), user2.getToken());

        assertEquals(receivedUser3.getEmail(), user3.getEmail());
        assertEquals(receivedUser3.getId(), user3.getId());
        assertEquals(receivedUser3.getToken(), user3.getToken());
    }
}
