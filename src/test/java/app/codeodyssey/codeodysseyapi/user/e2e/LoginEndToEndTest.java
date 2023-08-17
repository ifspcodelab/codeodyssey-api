package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.api.LoginRequest;
import app.codeodyssey.codeodysseyapi.user.api.LoginResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("tests for login e2e")
public class LoginEndToEndTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RefreshTokenRepository tokenRepository;

    @Value("${application.jwt.secret-key}")
    private String jwtSecret;

    @LocalServerPort
    Integer port;

    String url;
    RestTemplate restTemplate;

    @BeforeEach
    void beforeEach() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
        url = "http://localhost:%d/api/v1/login".formatted(port);
        restTemplate = new RestTemplate();
    }

    @Test
    @DisplayName("login given a valid request returns tokens")
    void login_givenAValidLoginRequest_returnsLoginResponse() {
        var name = "John Doe";
        var email = "john@email.com";
        var password = "$2a$10$oe40I38YdnKGq/QiH99kfOaJUY4QwMCSBDwpUR60iOXhD48/y/dDe";
        User user = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .password(password)
                .role(UserRole.STUDENT)
                .createdAt(Instant.now())
                .isValidated(true)
                .token(UUID.randomUUID().toString())
                .build());

        HttpEntity<LoginRequest> request = new HttpEntity<>(new LoginRequest("john@email.com", "123456"));

        LoginResponse response = restTemplate.postForObject(url, request, LoginResponse.class);

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.accessToken());
        Assertions.assertNotNull(response.refreshToken());

        Claims body = Jwts.parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(response.accessToken())
                .getBody();

        Assertions.assertEquals(body.getSubject(), user.getId().toString());
        Assertions.assertEquals(body.get("role"), "STUDENT");
        Assertions.assertEquals(body.getIssuer(), "code-odyssey");
        Assertions.assertEquals(body.get("name"), user.getName());
        Assertions.assertEquals(body.get("email"), user.getEmail());
    }

    @Test
    @DisplayName("should throw data integrity exception when tries to save an already saved user")
    void save_givenAnAlreadySavedUser_returnsException() {

        User user = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@email.com")
                .password("$2a$10$oe40I38YdnKGq/QiH99kfOaJUY4QwMCSBDwpUR60iOXhD48/y/dDe")
                .role(UserRole.STUDENT)
                .createdAt(Instant.now())
                .isValidated(true)
                .token(UUID.randomUUID().toString())
                .build());

        user.setId(UUID.randomUUID());

        Assertions.assertThrows(
                DataIntegrityViolationException.class,
                () -> userRepository.save(user),
                "should throw DataIntegrityViolationException");
    }

    @Test
    @DisplayName("login given an invalid email returns bad request")
    void login_givenAnInvalidEmail_returnsException() {
        HttpEntity<LoginRequest> request = new HttpEntity<>(new LoginRequest("john@email.com", "123456"));

        Assertions.assertThrows(
                HttpClientErrorException.Forbidden.class,
                () -> restTemplate.postForObject(url, request, LoginResponse.class),
                "should throw ForbiddenException");
    }

    @Test
    @DisplayName("login given an invalid password returns bad credentials")
    void login_givenAnInvalidPassword_returnsException() {
        var name = "John Doe";
        var email = "john@email.com";
        var password = "$2a$10$oe40I38YdnKGq/QiH99kfOaJUY4QwMCSBDwpUR60iOXhD48/y/dDe";
        User user = userRepository.save(User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .email(email)
                .password(password)
                .role(UserRole.STUDENT)
                .createdAt(Instant.now())
                .isValidated(true)
                .token(UUID.randomUUID().toString())
                .build());

        HttpEntity<LoginRequest> request = new HttpEntity<>(new LoginRequest("john@email.com", "654321"));

        Assertions.assertThrows(
                HttpClientErrorException.Forbidden.class,
                () -> restTemplate.postForObject(url, request, LoginResponse.class),
                "should throw ForbiddenException");
    }
}
