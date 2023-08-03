package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DisplayName("test for the CreateUserEndpoint")
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

    @MockBean
    private JavaMailSender mailSender;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

        @Test
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

            Optional<User> foundUserOptional = userRepository.findByEmail(command.email());

            Assertions.assertTrue(foundUserOptional.isPresent());

            User foundUser = foundUserOptional.get();

            verify(mailSender).send(any(SimpleMailMessage.class));
            Assertions.assertEquals(command.email(), foundUser.getEmail());
            Assertions.assertEquals(command.name(), foundUser.getName());
            Assertions.assertTrue(passwordEncoder.matches(command.password(), foundUser.getPassword()));
        }

    @Test
    @DisplayName("throws exception due the attempt to register a registered email")
    void post_givenUserAlreadyExists_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "Password#123");

        User existingUser = new User("sergio@example.com", "Sergio", "Password#123");
        userRepository.save(existingUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("User Already exists"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail").value("Email already exists"));

        Optional<User> foundUserOptional = userRepository.findByEmail(existingUser.getEmail());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertEquals(existingUser.getId(), foundUser.getId());
        Assertions.assertEquals(existingUser.getEmail(), foundUser.getEmail());
        Assertions.assertEquals(existingUser.isValidated(), foundUser.isValidated());
        Assertions.assertEquals(existingUser.getToken(), foundUser.getToken());
        Assertions.assertEquals(existingUser.getRole(), foundUser.getRole());
        Assertions.assertEquals(existingUser.getName(), foundUser.getName());
        Assertions.assertEquals(existingUser.getPassword().trim(), foundUser.getPassword().trim());
    }

    @Test
    @DisplayName("throws exception due to password pattern")
    void post_givenInvalidPassword_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "sergio@example.com", "Password123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Password problem"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail")
                        .value("The password must have at least one uppercase letter, one lowercase letter," +
                                " one number and one special character"));
    }

    @Test
    @DisplayName("throws exception due to invalid email format")
    void post_givenInvalidEmail_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Sergio", "s", "Password#123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Validation Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail")
                        .value(messageSource.getMessage(
                                "jakarta.validation.constraints.Email.message",
                                null,
                                LocaleContextHolder.getLocale())));
    }

    @Test
    @DisplayName("throws exception due to invalid name size")
    void post_givenInvalidName_exceptionThrown() throws Exception {
        CreateUserCommand command = new CreateUserCommand("Serg", "sergio@example.com", "Password#123");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(command)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Validation Error"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.detail")
                        .value(messageSource.getMessage(
                                "jakarta.validation.constraints.Size.message", null, LocaleContextHolder.getLocale())));
    }
}
