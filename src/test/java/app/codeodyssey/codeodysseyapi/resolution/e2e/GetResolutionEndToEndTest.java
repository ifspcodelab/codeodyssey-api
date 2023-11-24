package app.codeodyssey.codeodysseyapi.resolution.e2e;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
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
import app.codeodyssey.codeodysseyapi.resolution.api.ResolutionResponse;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
import app.codeodyssey.codeodysseyapi.resolution.util.ResolutionFactory;
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
@DisplayName("Tests for Get Resolution Endpoint")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class GetResolutionEndToEndTest {
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

    @Autowired
    private ResolutionRepository resolutionRepository;

    User student, professor;
    Course course;
    Invitation invitation;
    Enrollment enrollment;
    Activity activity;
    Resolution resolution;

    @BeforeEach
    void beforeEach() {
        student = UserFactory.createValidUser();
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);
        enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);
        activity = ActivityFactory.createValidActivityWithCourse(course);
        resolution = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);

        userRepository.save(student);
        userRepository.save(professor);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);
        activityRepository.save(activity);
        resolutionRepository.save(resolution);
    }

    @AfterEach
    void tearDown() {
        resolutionRepository.deleteAll();
        activityRepository.deleteAll();
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns an resolution when given a valid course id, activity id and resolution id with student logged in")
    void getResolutionEndpoint_givenValidCourseIdActivityIdAndResolutionId_returnsList() {
        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions/%s"
                .formatted(port, course.getId(), activity.getId(), resolution.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(null, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.GET, request, ResolutionResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("returns conflict when given an user that is not from the course")
    void getResolutionEndpoint_givenUserNotFromCourse_return409Conflict() {
        var studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions/%s"
                .formatted(port, course.getId(), activity.getId(), resolution.getId());
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
    @DisplayName("returns conflict when given an activity that is not from the course")
    void getResolutionEndpoint_givenActivityNotFromCourse_return409Conflict() {
        Course courseB = CourseFactory.createValidCourseWithProfessor(professor);
        courseRepository.save(courseB);

        Activity activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions/%s"
                .formatted(port, course.getId(), activityB.getId(), resolution.getId());
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
    @DisplayName("returns conflict when given an resolution that is not from the activity")
    void getResolutionEndpoint_givenResolutionNotFromActivity_return409Conflict() {
        Activity activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityRepository.save(activityB);

        Resolution resolutionB = ResolutionFactory.createValidResolutionWithActivityAndStudent(activityB, student);
        resolutionRepository.save(resolutionB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions/%s"
                .formatted(port, course.getId(), activity.getId(), resolutionB.getId());
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
    @DisplayName("returns conflict when given an user that did not create the resolution")
    void getResolutionEndpoint_givenResolutionNotFromStudent_return409Conflict() {
        User studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        Enrollment enrollmentB = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        enrollmentRepository.save(enrollmentB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions/%s"
                .formatted(port, course.getId(), activity.getId(), resolution.getId());
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
}
