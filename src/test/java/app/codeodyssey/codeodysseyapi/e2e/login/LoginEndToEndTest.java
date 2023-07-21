package app.codeodyssey.codeodysseyapi.e2e.login;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.user.api.LoginRequest;
import app.codeodyssey.codeodysseyapi.user.api.LoginResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ContextConfiguration(initializers = { DatabaseContainerInitializer.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoginEndToEndTest {
    @Autowired
    UserRepository userRepository;

    @Value("${application.jwt.secret-key}")
    private String jwtSecret;

    @LocalServerPort
    Integer port;

    String url;
    RestTemplate restTemplate;

    @BeforeEach
    void beforeEach() {
        userRepository.deleteAll();
        url = "http://localhost:%d/api/v1/login".formatted(port);
        restTemplate = new RestTemplate();
    }

    @Test
    void loginSuccess() {
        var name = "John Due";
        var email = "john@email.com";
        var password = "$2a$10$oe40I38YdnKGq/QiH99kfOaJUY4QwMCSBDwpUR60iOXhD48/y/dDe";
        User user = userRepository.save(new User(name, email, password));

        HttpEntity<LoginRequest> request = new HttpEntity<>(
            new LoginRequest("john@email.com", "123456")
        );

        LoginResponse response = restTemplate.postForObject(url, request, LoginResponse.class);

        // check if response is not null
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.accessToken());
        Assertions.assertNotNull(response.refreshToken());

        // check generate token
        Claims body = Jwts
                .parserBuilder()
                .setSigningKey(jwtSecret)
                .build()
                .parseClaimsJws(response.accessToken())
                .getBody();

        Assertions.assertEquals(body.getSubject(), user.getEmail());
        Assertions.assertEquals(body.get("role"), "STUDENT");

        // falta iss,
        // colocar id em sub
        // colocar as claims name, email
    }

    @Test
    void tryToSaveSameUser() {
        var name = "John Due";
        var email = "john@email.com";
        var password = "$2a$10$oe40I38YdnKGq/QiH99kfOaJUY4QwMCSBDwpUR60iOXhD48/y/dDe";

        userRepository.save(new User(name, email, password));
    }


}
