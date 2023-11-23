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
@DisplayName("Tests for Get Resolutions Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetResolutionsServiceTest {
    @Autowired
    private GetResolutionsService getResolutionsService;

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
    @DisplayName("returns list when given when given a valid course id and activity id; student has to be enrolled")
    void execute_givenValidCourseIdAndActivityIdWithStudentLoggedIn_returnList() {
        var resolutionList = assertDoesNotThrow(() -> getResolutionsService.execute(course.getId(), activity.getId(), student.getEmail()));

        assertThat(resolutionList).isNotEmpty();
    }

    @Test
    @DisplayName("returns not found when given an invalid user email")
    void execute_givenInvalidUserEmail_return404NotFound() {
        assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> getResolutionsService.execute(course.getId(), activity.getId(), "Email"));
    }

    @Test
    @DisplayName("returns conflict when given an user that is not from the course")
    void execute_givenUserNotFromCourse_return409Conflict() {
        User studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        assertThatExceptionOfType(UserNotAssociatedException.class)
                .isThrownBy(() -> getResolutionsService.execute(course.getId(), activity.getId(), studentB.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an activity that is not from the course")
    void execute_givenActivityNotFromCourse_return409Conflict() {
        Course courseB = CourseFactory.createValidCourseWithProfessor(professor);
        courseRepository.save(courseB);

        Activity activityB = ActivityFactory.createValidActivityBWithCourse(courseB);
        activityRepository.save(activityB);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getResolutionsService.execute(course.getId(), activityB.getId(), student.getEmail()));
    }
}
