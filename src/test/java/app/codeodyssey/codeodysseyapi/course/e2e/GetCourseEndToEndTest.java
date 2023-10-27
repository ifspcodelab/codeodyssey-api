package app.codeodyssey.codeodysseyapi.course.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.token.data.RefreshTokenRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.AuthenticationTokenPairFactory;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Get Course End to End tests")
@Testcontainers
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GetCourseEndToEndTest {
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

    @DisplayName("getCourseEndpoint returns a course when an user is admin")
    @Test
    void getCoursesEndpoint_givenAdmin_returnsCourse() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var admin = UserFactory.sampleUserAdmin();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(admin);
        userRepository.saveAll(List.of(professor, admin));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(url+"/"+course.getId(), HttpMethod.GET, request, CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @DisplayName("getCourseEndpoint returns a course when an user is the course professor")
    @Test
    void getCoursesEndpoint_givenProfessor_returnsCourse() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var admin = UserFactory.sampleUserAdmin();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(professor);
        userRepository.saveAll(List.of(professor, admin));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);

        var response = restTemplate.exchange(url+"/"+course.getId(), HttpMethod.GET, request, CourseResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @DisplayName("getCourseEndpoint returns unauthorized when an user is not in course")
    @Test
    void getCoursesEndpoint_givenNonAdmin_returnsUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var student = UserFactory.sampleUserStudent();
        var authenticationTokenPair = AuthenticationTokenPairFactory.sampleAuthenticationTokenPair(student);
        userRepository.saveAll(List.of(professor, student));
        courseRepository.save(course);
        refreshTokenRepository.save(authenticationTokenPair.getRefreshToken());
        headers.setBearerAuth(authenticationTokenPair.getAccessToken());
        HttpEntity<?> request = new HttpEntity<>(null, headers);

        var throwable = catchThrowable(() -> restTemplate.exchange(url+"/"+course.getId(), HttpMethod.GET,
                request, String.class));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(HttpClientErrorException.class);
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(httpException.getResponseBodyAsString()).isNotNull();
    }
}
