package app.codeodyssey.codeodysseyapi.course.e2e;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.catchThrowable;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.AuthenticationTokenPairFactory;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Get Courses End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GetCoursesEndToEndTest {
    @LocalServerPort
    Integer port;

    String url;

    RestTemplate restTemplate;

    HttpHeaders headers;

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
        headers = new HttpHeaders();
    }

    @AfterEach
    void afterEach() {
        refreshTokenRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("getCoursesEndpoint return courses when an user is admin")
    @Test
    void getCoursesEndpoint_givenAdmin_returnList() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var admin = UserFactory.sampleUserAdmin();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(admin);
        userRepository.saveAll(List.of(professor, admin));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @DisplayName("getCoursesEndpoint return unauthorized when an user is not an admin")
    @Test
    void getCoursesEndpoint_givenNonAdmin_returnUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var student = UserFactory.sampleUserStudent();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(student);
        userRepository.saveAll(List.of(professor, student));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(httpException.getResponseBodyAsString()).isNotNull();
    }
}
