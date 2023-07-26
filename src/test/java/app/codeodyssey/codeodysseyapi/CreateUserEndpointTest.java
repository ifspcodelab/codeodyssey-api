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
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@DisplayName("test for the CreateUserEndpointService")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CreateUserEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MessageSource messageSource;

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

        User foundUser = userRepository.getUserByEmail(command.email());

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(command.email(), foundUser.getEmail());
        Assertions.assertEquals(command.name(), foundUser.getName());
        Assertions.assertTrue(passwordEncoder.matches(command.password(), foundUser.getPassword()));
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
        Assertions.assertEquals(existingUser.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(existingUser.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(existingUser.getToken(), foundUser.getToken());
        Assertions.assertEquals(existingUser.getRole(), foundUser.getRole());
        Assertions.assertEquals(existingUser.getName(), foundUser.getName());
        Assertions.assertEquals(existingUser.getPassword(), foundUser.getPassword());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value(messageSource.getMessage("jakarta.validation.constraints.Email.message", null, LocaleContextHolder.getLocale())));
    }


}
