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

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("Tests for Create Resolution Endpoint")
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
public class CreateResolutionEndToEndTest {
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
        resolution = ResolutionFactory.createValidResolutionWithActivity(activity);

        userRepository.save(student);
        userRepository.save(professor);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);
        activityRepository.save(activity);
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
    @DisplayName("register a new resolution when given a valid course id and activity id, student has to be enrolled")
    void createResolutionEndpoint_givenValidCourseIdAndActivityIdWithStudentLoggedIn_return201Created() {
        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    @DisplayName("returns not found when given an invalid activity id")
    void createResolutionEndpoint_givenInvalidActivityId_returns404NotFound() {
        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), UUID.randomUUID());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("returns conflict when given a activity id that is not from course")
    void createResolutionEndpoint_givenActivityNotFromCourse_returns409Conflict() {
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        courseRepository.save(courseB);

        var activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), activityB.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when given an student that is not enrolled in the course")
    void createResolutionEndpoint_givenStudentNotEnrolled_returns409Conflict() {
        var studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), activity.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(studentB);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when the resolution submit date is before the activity start date")
    void createResolutionEndpoint_givenSubmitDateBeforeActivityStartDate_return409Conflict() {
        var activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityB.setStartDate(resolution.getSubmitDate().plusSeconds(300));
        activityRepository.save(activityB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), activityB.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("returns conflict when the resolution submit date is after the activity end date")
    void createResolutionEndpoint_givenSubmitDateAfterActivityEndDate_return409Conflict() {
        var activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityB.setEndDate(resolution.getSubmitDate().minusSeconds(300));
        activityRepository.save(activityB);

        url = "http://localhost:%d/api/v1/courses/%s/activities/%s/resolutions"
                .formatted(port, course.getId(), activityB.getId());
        restTemplate = new RestTemplate();
        httpHeaders = new HttpHeaders();

        var token = AccessTokenFactory.sampleAccessToken(student);
        httpHeaders.setBearerAuth(token);
        HttpEntity<?> request = new HttpEntity<>(resolution, httpHeaders);

        var throwable = catchThrowable(() -> restTemplate.exchange(url, HttpMethod.POST, request, String.class));

        assertThat(throwable).isNotNull();
        HttpClientErrorException httpException = (HttpClientErrorException) throwable;
        assertThat(httpException.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
}
