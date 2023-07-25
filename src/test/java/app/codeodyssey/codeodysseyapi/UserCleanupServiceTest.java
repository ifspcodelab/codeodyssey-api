package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.UserCleanupService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class UserCleanupServiceTest {
    @Autowired
    private UserCleanupService userCleanupService;

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
    void testExecute_cleanNonValidatedUser() {
        User user = new User("Sergio", "sergio@example.com", "password");
        user.setCreatedAt(user.getCreatedAt().minus(180, ChronoUnit.SECONDS));
        userRepository.save(user);

        userCleanupService.cleanupUser();

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNull();

    }

    @Test
    void testExecute_dontCleanValidUser() {
        User user = new User("Sergio", "sergio@example.com", "password");
        userRepository.save(user);

        userCleanupService.cleanupUser();

        User receivedUser = userRepository.getUserByEmail(user.getEmail());

        assertThat(userRepository.getUserByEmail(user.getEmail())).isNotNull();
        assertEquals(receivedUser.getEmail(), user.getEmail());
    }
}
