package app.codeodyssey.codeodysseyapi.invitation.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Create Invitation End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateInvitationEndToEndTest {
    @LocalServerPort
    Integer port;

    String url;

    RestTemplate restTemplate;

    HttpHeaders httpHeaders;

    @BeforeEach
    void beforeEach() {
        url = "http://localhost:%d/api/v1/courses".formatted(port);
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }
}
