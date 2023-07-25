package app.codeodyssey.codeodysseyapi.course.e2e;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Get Professor's Courses End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GetProfessorCoursesEndToEndTest {
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
        url = "http://localhost:%d/api/v1/users".formatted(port);
        restTemplate = new RestTemplate();
        headers = new HttpHeaders();
    }

    @AfterEach
    void afterEach() {
        refreshTokenRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("getProfessorCoursesEndpoint returns courses when an user is a professor")
    @Test
    void getProfessorCoursesEndpoint_givenProfessor_returnsList() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professor);
        userRepository.save(professor);
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        url += "/%s/courses".formatted(professor.getId());

        var response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
    }

    @DisplayName("getProfessorCoursesEndpoint returns unauthorized when an user is not a professor")
    @Test
    void getProfessorCoursesEndpoint_givenNonProfessor_returnsUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var student = UserFactory.sampleUserStudent();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(student);
        userRepository.saveAll(List.of(professor, student));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);
        url += "/%s/courses".formatted(student.getId());

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(httpException.getResponseBodyAsString()).isNotNull();
    }
}
