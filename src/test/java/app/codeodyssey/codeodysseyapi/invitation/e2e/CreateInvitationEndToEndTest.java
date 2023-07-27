package app.codeodyssey.codeodysseyapi.invitation.e2e;

import static org.assertj.core.api.Assertions.*;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.service.InvitationCreateCommand;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.AuthenticationTokenPairFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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

    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void beforeEach() {
        url = "http://localhost:%d/api/v1/courses".formatted(port);
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @DisplayName("createInvitationEndpoint returns invitation when an user is the given course's professor")
    @Test
    void createInvitationEndpoint_givenCourseAndProfessor_returnsInvitation() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var authTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professor);
        userRepository.save(professor);
        courseRepository.save(course);
        refreshTokenRepository.save(authTokenPair.getRefreshToken());
        httpHeaders.setBearerAuth(authTokenPair.getAccessToken());
        HttpEntity<?> request =
                new HttpEntity<>(new InvitationCreateCommand(LocalDate.now().plusMonths(1)), httpHeaders);
        url += "/%s/invitations".formatted(course.getId());

        var response = restTemplate.exchange(url, HttpMethod.POST, request, InvitationResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }
}
