package app.codeodyssey.codeodysseyapi.activity.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.AuthenticationTokenPairFactory;
import org.junit.jupiter.api.AfterEach;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("Create Activity End to End tests")
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CreateActivityEndToEndTest {

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

    @Autowired
    ActivityRepository activityRepository;

    @BeforeEach
    void beforeEach() {
        url = "http://localhost:%d/api/v1/courses".formatted(port);
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @AfterEach
    void afterEach() {
        activityRepository.deleteAll();
        refreshTokenRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createActivityEndpoint returns activity given a request")
    void createActivityEndpoint_givenRequest_returnsActivity() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var authTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professor);
        var activity = ActivityFactory.sampleActivity(course);
        userRepository.save(professor);
        courseRepository.save(course);
        refreshTokenRepository.save(authTokenPair.getRefreshToken());
        activityRepository.save(activity);
        httpHeaders.setBearerAuth(authTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(activity, httpHeaders);
        url += "/%s/activities".formatted(course.getId());

        var response = assertDoesNotThrow(
                () -> restTemplate.exchange(url, HttpMethod.POST, request, ActivityResponse.class));

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isInstanceOf(ActivityResponse.class);
    }
}
