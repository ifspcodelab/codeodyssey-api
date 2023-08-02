package app.codeodyssey.codeodysseyapi.user.api;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import app.codeodyssey.codeodysseyapi.common.exception.TokenProblem;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the ValidateUserEndpoint")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ValidateUserEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

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
    @DisplayName("patch a valid user to validate and return its response")
    void patch_givenValidUser_returnsUserResponse() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("try to patch a user with an expired token and throw exception")
    void patch_givenUserWithExpiredToken_exceptionThrown() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Token problem"))
                .andExpect(jsonPath("$.detail").value(TokenProblem.EXPIRED.getMessage()));

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertFalse(foundUser.isValidated());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("try to patch a user with a non-existent token and throw an exception")
    void patch_givenNonexistentToken_exceptionThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Token problem"))
                .andExpect(jsonPath("$.detail").value(TokenProblem.NONEXISTENT.getMessage()));
    }

    @Test
    @DisplayName("try to validate a user already validated and throw an exception")
    void patch_givenUserAlreadyValidated_exceptionThrown() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setValidated(true);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Validation"))
                .andExpect(jsonPath("$.detail").value("User is already validated"));

        Optional<User> foundUserOptional = userRepository.findByEmail(user.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertEquals(user.getId(), foundUser.getId());
        Assertions.assertEquals(user.getToken(), foundUser.getToken());
        Assertions.assertEquals(user.getName(), foundUser.getName());
        Assertions.assertEquals(user.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(user.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(user.getRole(), foundUser.getRole());
        Assertions.assertEquals(user.getPassword().trim(), foundUser.getPassword().trim());
    }
}
