package app.codeodyssey.codeodysseyapi.user.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.enrollment.util.EnrollmentFactory;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
import app.codeodyssey.codeodysseyapi.token.util.AccessTokenFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Tests for Course Students Endpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class GetCourseStudentsEndToEndTest {
    @LocalServerPort
    Integer port;
    String url;
    RestTemplate restTemplate;
    HttpHeaders httpHeaders;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    User professor, student;
    Course course;
    Invitation invitation;
    Enrollment enrollment;

    @BeforeEach
    void beforeEach() {
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);
        student = UserFactory.sampleUserStudent();
        enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);

        userRepository.save(professor);
        userRepository.save(student);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);
    }

    @AfterEach
    void tearDown() {
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns a user list when given a valid professor id and course slug")
    void getCourseStudentsEndpoint_givenValidProfessorIdAndSlug_returnsList() {
        url = "http://localhost:%d/api/v1/users/%s/courses/%s/students".formatted(port, professor.getId(), course.getSlug());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, List.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("returns an exception when given a an invalid user id")
    void getCourseStudentsEndpoint_givenInvalidUserId_returns404NotFound() {
        url = "http://localhost:%d/api/v1/users/%s/courses/%s/students".formatted(port, UUID.randomUUID(), course.getSlug());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("returns an exception when given a an invalid professor role")
    void getCourseStudentsEndpoint_givenInvalidProfessorRole_returns403Forbidden() {
        var user = UserFactory.createValidUser();
        userRepository.save(user);

        url = "http://localhost:%d/api/v1/users/%s/courses/%s/students".formatted(port, user.getId(), course.getSlug());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(user);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("returns an exception when given a an invalid professor id and course slug")
    void getCourseStudentsEndpoint_givenInvalidProfessorRole_returns401Conflict() {
        var professorB = UserFactory.sampleUserProfessorB();
        userRepository.save(professorB);

        url = "http://localhost:%d/api/v1/users/%s/courses/%s/students".formatted(port, professorB.getId(), course.getSlug());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(professorB);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
