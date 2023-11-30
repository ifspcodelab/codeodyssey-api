package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.UserNotAssociatedException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
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
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("Tests for Get Resolution Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetResolutionServiceTest {
    @Autowired
    private GetResolutionService getResolutionService;

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
    @DisplayName("returns ResolutionResponse when given when given a valid course id, activity id and resolution id; student has to be enrolled")
    void execute_givenValidCourseIdAndActivityIdAndResolutionIdWithStudentLoggedIn_returnResolutionResponse() {
        var resolutionResponse = assertDoesNotThrow(() -> getResolutionService.execute(course.getId(), activity.getId(), resolution.getId(), student.getEmail()));

        assertThat(resolutionResponse).isNotNull();
        assertThat(resolutionResponse).isInstanceOf(ResolutionResponse.class);
    }

    @Test
    @DisplayName("returns not found when given an invalid user email")
    void execute_givenInvalidUserEmail_return404NotFound() {
        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> getResolutionService.execute(course.getId(), activity.getId(), resolution.getId(), "Email"));
    }

    @Test
    @DisplayName("returns conflict when given an user that is not from the course")
    void execute_givenUserNotFromCourse_return409Conflict() {
        User studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        assertThatExceptionOfType(UserNotAssociatedException.class)
                .isThrownBy(() -> getResolutionService.execute(course.getId(), activity.getId(), resolution.getId(), studentB.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an activity that is not from the course")
    void execute_givenActivityNotFromCourse_return409Conflict() {
        Course courseB = CourseFactory.createValidCourseWithProfessor(professor);
        courseRepository.save(courseB);

        Activity activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getResolutionService.execute(course.getId(), activityB.getId(), resolution.getId(), student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an resolution that is not from the activity")
    void execute_givenResolutionNotFromActivity_return409Conflict() {
        Activity activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityRepository.save(activityB);

        Resolution resolutionB = ResolutionFactory.createValidResolutionWithActivityAndStudent(activityB, student);
        resolutionRepository.save(resolutionB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getResolutionService.execute(course.getId(), activity.getId(), resolutionB.getId(), student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an user that did not create the resolution")
    void execute_givenResolutionNotFromStudent_return409Conflict() {
        User studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        Enrollment enrollmentB = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        enrollmentRepository.save(enrollmentB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getResolutionService.execute(course.getId(), activity.getId(), resolution.getId(), studentB.getEmail()));
    }
}
