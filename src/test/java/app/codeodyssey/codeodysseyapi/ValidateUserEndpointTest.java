package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.common.exception.TokenProblem;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
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
    void testPatch_ValidateUser_Success() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value(user.getName()))
                .andExpect(jsonPath("$.email").value(user.getEmail()));

        User foundUser = userRepository.getUserByEmail(user.getEmail());

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getId(), foundUser.getId());
    }

    @Test
    void testPatch_UserWithExpiredToken_ExceptionThrown() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setCreatedAt(user.getCreatedAt().minus(expirationTime + 1, ChronoUnit.SECONDS));
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Token problem"))
                .andExpect(jsonPath("$.detail").value(TokenProblem.EXPIRED.getMessage()));
    }

    @Test
    void testPatch_NonexistentToken_ExceptionThrown() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + UUID.randomUUID()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Token problem"))
                .andExpect(jsonPath("$.detail").value(TokenProblem.NONEXISTENT.getMessage()));
    }

    @Test
    void testPatch_UserAlreadyValidated_ExceptionThrown() throws Exception {
        User user = new User("sergio@example.com", "sergio", "password");
        user.setValidated(true);
        userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/v1/users/confirmation/" + user.getToken()))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.title").value("Validation"))
                .andExpect(jsonPath("$.detail").value("User is already validated"));
    }
}
