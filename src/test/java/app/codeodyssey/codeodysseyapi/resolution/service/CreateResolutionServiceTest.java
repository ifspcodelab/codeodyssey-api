package app.codeodyssey.codeodysseyapi.resolution.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
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
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionRepository;
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

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@DisplayName("Tests for Create Resolution Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CreateResolutionServiceTest {
    @Autowired
    private CreateResolutionService createResolutionService;

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
    CreateResolutionCommand resolutionCommand;

    @BeforeEach
    void beforeEach() {
        student = UserFactory.createValidUser();
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);
        enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);
        activity = ActivityFactory.createValidActivityWithCourse(course);
        resolutionCommand = new CreateResolutionCommand("ResolutionFile");

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
    @DisplayName("returns ResolutionResponse when given when given a valid course id, activity id and resolution command; student has to be enrolled")
    void execute_givenValidCourseIdAndActivityIdAndResolutionCommandWithStudentLoggedIn_returnResolutionResponse() {
        var resolution = assertDoesNotThrow(() -> createResolutionService.execute(course.getId(), activity.getId(), resolutionCommand, student.getEmail()));

        assertThat(resolution).isNotNull();
        assertThat(resolution).isInstanceOf(ResolutionResponse.class);
    }

    @Test
    @DisplayName("returns not found when given an invalid user email")
    void execute_givenInvalidUserEmail_return404NotFound() {
        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), activity.getId(), resolutionCommand, "Email"));
    }

    @Test
    @DisplayName("returns not found when given an invalid activity id")
    void execute_givenInvalidActivityId_return404NotFound() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), UUID.randomUUID(), resolutionCommand, student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given a activity id that is not from course")
    void execute_givenActivityNotFromCourse_return409Conflict() {
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        courseRepository.save(courseB);

        var activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), activityB.getId(), resolutionCommand, student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an student that is not enrolled in the course")
    void execute_givenStudentNotEnrolled_return409Conflict() {
        var studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        assertThatExceptionOfType(UserNotAssociatedException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), activity.getId(), resolutionCommand, studentB.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when the resolution submit date is before the activity start date")
    void execute_givenSubmitDateBeforeActivityStartDate_return409Conflict() {
        var activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityB.setStartDate(Instant.now().plusSeconds(3000));
        activityRepository.save(activityB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), activityB.getId(), resolutionCommand, student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when the resolution submit date is after the activity end date")
    void execute_givenSubmitDateAfterActivityEndDate_return409Conflict() {
        var activityB = ActivityFactory.createValidActivityBWithCourse(course);
        activityB.setEndDate(Instant.now().minusSeconds(3000));
        activityRepository.save(activityB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createResolutionService.execute(course.getId(), activityB.getId(), resolutionCommand, student.getEmail()));
    }
}
