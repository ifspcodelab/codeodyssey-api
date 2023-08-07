package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("test for the ValidateUserEndpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class ValidateUserEndToEndTest {
    @Autowired
    private UserRepository userRepository;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    private RestTemplate restTemplate;

    @LocalServerPort
    private Integer port;

    private String url;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("patch a valid user to validate and return UserResponse")
    void patch_givenValidUser_returnsUserResponse() {
        User user = new User("sergio@example.com", "sergio", "password");
        userRepository.save(user);

        url = "http://localhost:" + port + "/api/v1/users/confirmation/" + user.getToken();

        restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        HttpHeaders  headers = new HttpHeaders();

        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserResponse> responseEntity = restTemplate.exchange(url, HttpMethod.PATCH, requestEntity, UserResponse.class);

        UserResponse response = responseEntity.getBody();

        Assertions.assertNotNull(requestEntity.getBody());
        Assertions.assertNotNull(response);

        Optional<User> foundUserOptional = userRepository.findByEmail(response.email());

        Assertions.assertTrue(foundUserOptional.isPresent());

        User foundUser = foundUserOptional.get();

        Assertions.assertTrue(foundUser.isValidated());
        Assertions.assertEquals(foundUser.getId(), response.id());
        Assertions.assertEquals(foundUser.getRole(), response.role());
        Assertions.assertEquals(foundUser.getName(), response.name());
    }
}