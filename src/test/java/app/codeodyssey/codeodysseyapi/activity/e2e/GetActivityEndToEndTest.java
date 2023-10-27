package app.codeodyssey.codeodysseyapi.activity.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Tests for Get Activity Endpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class GetActivityEndToEndTest {
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

    @Autowired
    private ActivityRepository activityRepository;

    User student, professor;
    Course course;
    Invitation invitation;
    Enrollment enrollment;
    Activity activity;

    @BeforeEach
    void beforeEach() {
        student = UserFactory.createValidUser();
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);
        enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);
        activity = ActivityFactory.createValidActivityWithCourse(course);

        userRepository.save(student);
        userRepository.save(professor);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);
        activityRepository.save(activity);
    }

    @AfterEach
    void tearDown() {
        activityRepository.deleteAll();
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns an activity when given a valid course id and activity id, student has to be enrolled")
    void getActivityEndpoint_givenValidCourseIdAndActivityIdWithStudentLoggedIn_returnsActivity() {
        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, course.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, ActivityResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("returns an activity when given a valid course id and activity id, professor has to be the creator")
    void getActivityEndpoint_givenValidCourseIdAndActivityIdWithProfessorLoggedIn_returnsActivity() {
        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, course.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(professor);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, ActivityResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("returns conflict when given an invalid course id")
    void getActivityEndpoint_givenInvalidCourseId_returns409Conflict() {
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        courseRepository.save(courseB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, courseB.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when given an invalid activity id")
    void getActivityEndpoint_givenInvalidActivityId_returns409Conflict() {
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        courseRepository.save(courseB);

        var activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, course.getId(), activityB.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when given an student that is not enrolled in the course")
    void getActivityEndpoint_givenStudentNotEnrolled_returns409Conflict() {
        var studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, course.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(studentB);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.GET, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when given an professor that did not created the activity")
    void getActivityEndpoint_givenProfessorDidNotCreateActivity_returns409Conflict() {
        var professorB = UserFactory.sampleUserProfessorB();
        userRepository.save(professorB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s"
                .formatted(port, course.getId(), activity.getId());
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
