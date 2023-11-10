package app.codeodyssey.codeodysseyapi.activity.service;

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
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
@DisplayName("Tests for Get Activity Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetActivityServiceTest {
    @Autowired
    GetActivityService getActivityService;

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
    void getActivityService_givenValidCourseIdAndActivityIdWithStudentLoggedIn_returnsActivity() {
        Throwable serviceThrowable =
                catchThrowable(() -> getActivityService.execute(course.getId(), activity.getId(), student.getEmail()));

        assertThat(serviceThrowable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("returns an activity when given a valid course id and activity id, professor has to be enrolled")
    void getActivityService_givenValidCourseIdAndActivityIdWithProfessorLoggedIn_returnsActivity() {
        Throwable serviceThrowable =
                catchThrowable(() -> getActivityService.execute(course.getId(), activity.getId(), professor.getEmail()));

        assertThat(serviceThrowable).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("returns not found when user is not logged in")
    void getActivityService_givenUserNotLoggedIn_returns404NotFound() {
        Assertions.assertThatExceptionOfType(EmailNotFoundException.class)
                .isThrownBy(() -> getActivityService.execute(course.getId(), activity.getId(), "email-invalid"));
    }

    @Test
    @DisplayName("returns conflict when given invalid course Id")
    void getActivityService_givenInvalidCourseId_returns401Conflict() {
        Assertions.assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getActivityService.execute(UUID.randomUUID(), activity.getId(), professor.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given invalid activity Id")
    void getActivityService_givenInvalidActivityId_returns401Conflict() {
        Assertions.assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getActivityService.execute(course.getId(), UUID.randomUUID(), student.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an student not associated to course")
    void getActivityService_givenStudentNotAssociated_returns401Conflict() {
        var studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        Assertions.assertThatExceptionOfType(UserNotAssociatedException.class)
                .isThrownBy(() -> getActivityService.execute(course.getId(), activity.getId(), studentB.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an professor not associated to course")
    void getActivityService_givenProfessorNotAssociated_returns401Conflict() {
        var professorB = UserFactory.sampleUserProfessorB();
        userRepository.save(professorB);

        Assertions.assertThatExceptionOfType(UserNotAssociatedException.class)
                .isThrownBy(() -> getActivityService.execute(course.getId(), activity.getId(), professorB.getEmail()));
    }
}
