package app.codeodyssey.codeodysseyapi.course.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.token.util.AccessTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.UUID;
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
@DisplayName("Tests for Create Course Endpoint")
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CreateCourseEndToEndTest {
    @LocalServerPort
    Integer port;

    String url;
    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    User professor;
    Course course;

    @BeforeEach
    public void setUp() {
        professor = UserFactory.createValidProfessor();
        userRepository.save(professor);
        course = CourseFactory.createValidCourseWithProfessor(professor);

        url = "http://localhost:%d/api/v1/users/%s/courses".formatted(port, professor.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("register a new course when given a valid course request")
    void createCourse_givenValidCourseRequest_return201Created() {
        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(course, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("returns not found when given an invalid user id")
    void createCourse_givenInvalidUserId_return404NotFound() {
        url = "http://localhost:%d/api/v1/users/%s/courses".formatted(port, UUID.randomUUID());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(course, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("returns forbidden when given a role that is not professor")
    void createCourse_givenInvalidProfessorRole_returns403Forbidden() {
        var user = UserFactory.createValidUser();
        userRepository.save(user);

        url = "http://localhost:%d/api/v1/users/%s/courses".formatted(port, user.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(user);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(course, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("returns conflict when given an existing course")
    void createCourse_givenExistingCourse_return409Conflict() {
        courseRepository.save(course);

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        var existingCourse = CourseFactory.createValidCourseWithProfessor(professor);
        HttpEntity<?> request = new HttpEntity<>(existingCourse, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when the course start date is before the current date")
    void createCourse_givenStartDateBeforeCurrentDate_return409Conflict() {
        var course = new Course("CourseName", "Slug", LocalDate.of(1000, 01, 01), LocalDate.now(), professor);

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(course, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when the course end date is before the start date")
    void createCourse_givenEndDateBeforeStartDate_return409Conflict() {
        var course = new Course("CourseName", "Slug", LocalDate.now(), LocalDate.of(1000, 01, 01), professor);

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(course, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
