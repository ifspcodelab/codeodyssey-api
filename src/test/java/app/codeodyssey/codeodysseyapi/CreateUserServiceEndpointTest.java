package app.codeodyssey.codeodysseyapi;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the CreateUserEndpointService")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CreateUserServiceEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    @Transactional
    @DisplayName("post a valid user")
    void post_givenValidUser_returnsUser() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(command.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(command.email()));

        User user = userRepository.getUserByEmail(command.email());
        Assertions.assertNotNull(user);
        Assertions.assertEquals(command.email(), user.getEmail());
    }


    @Test
    @Transactional
    @DisplayName("throws exception due the attempt to register a registered email")
    void post_givenUserAlreadyExists_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com",
                "Password#123");

        User existingUser = new User("sergio@example.com", "Sergio", "Password#123");
        userRepository.save(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("User Already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Email already exists"));

        User foundUser = userRepository.getUserByEmail(existingUser.getEmail());
        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(existingUser.getId(), foundUser.getId());
    }

    @Test
    @Transactional
    @DisplayName("throws exception due to weak password")
    void post_givenInvalidPassword_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com",
                "Password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Password problem"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("The password must " +
                        "contain at least one uppercase letter, one lowercase letter, one number, " +
                        "one special character, " +
                        "and be between 8 and 64 characters."));
    }

    @Test
    @Transactional
    @DisplayName("throws exception due to invalid email format")
    void post_givenInvalidEmail_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "s",
                "Password#123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Validation Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Invalid email format"));
    }
}