package app.codeodyssey.codeodysseyapi.resolution.data;

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
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("Tests for Invitation Repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class ResolutionRepositoryTest {
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
        resolutionRepository.deleteAll();
        activityRepository.deleteAll();
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
        resolutionRepository.deleteAll();
    }

    @Test
    @DisplayName("returns true when the resolution from an student and an activity have the waiting status")
    void existsByStudentIdAndActivityIdAndStatus_givenResolutionWithStatusWaiting_returnTrue() {
        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByStudentIdAndActivityIdAndStatus(student.getId(), activity.getId(), ResolutionStatus.WAITING_FOR_RESULTS);

        assertTrue(exists);
    }

    @Test
    @DisplayName("returns false when the resolution from an student and an activity don't have the waiting status")
    void existsByStudentIdAndActivityIdAndStatus_givenNoResolutionWithStatusWaiting_returnFalse() {
        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudentAndExecutedSuccessStatus(activity, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByStudentIdAndActivityIdAndStatus(student.getId(), activity.getId(), ResolutionStatus.WAITING_FOR_RESULTS);

        assertFalse(exists);
    }

    @Test
    @DisplayName("returns empty when the student don't have any resolutions in an activity")
    void findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate_givenStudentAndActivityWithNoResolutions_returnEmpty() {
        List<Resolution> resolutions = resolutionRepository.findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate(student.getId(), course.getId(), activity.getId());

        assertThat(resolutions).isEmpty();
    }

    @Test
    @DisplayName("returns list when the student have one resolution in an activity")
    void findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate_givenStudentAndActivityWithOneResolution_returnList() {
        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);
        resolutionRepository.save(resolution);

        List<Resolution> resolutions = resolutionRepository.findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate(student.getId(), course.getId(), activity.getId());

        assertThat(resolutions).isNotEmpty();
        assertThat(resolutions).hasSize(1);
    }

    @Test
    @DisplayName("returns list when the student have two resolutions in an activity")
    void findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate_givenStudentAndActivityWithTwoResolutions_returnList() {
        Resolution resolutionA = ResolutionFactory.createValidResolutionWithActivityAndStudentAndExecutedSuccessStatus(activity, student);
        resolutionRepository.save(resolutionA);

        Resolution resolutionB = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);
        resolutionRepository.save(resolutionB);

        List<Resolution> resolutions = resolutionRepository.findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate(student.getId(), course.getId(), activity.getId());

        assertThat(resolutions).isNotEmpty();
        assertThat(resolutions).hasSize(2);
    }

    @Test
    @DisplayName("returns true when the resolution is from the activity")
    void existsByActivityIdAndId_givenResolutionIdAndActivityId_returnTrue() {
        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByActivityIdAndId(activity.getId(), resolution.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("returns false when the resolution is not from the activity")
    void existsByActivityIdAndId_givenResolutionIdAndActivityId_returnFalse() {
        Activity activityB = ActivityFactory.createValidActivityWithCourse(course);
        activityRepository.save(activityB);

        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudentAndExecutedSuccessStatus(activityB, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByStudentIdAndActivityIdAndStatus(student.getId(), activity.getId(), ResolutionStatus.WAITING_FOR_RESULTS);

        assertFalse(exists);
    }

    @Test
    @DisplayName("returns true when the student created the resolution")
    void existsByStudentIdAndId_givenResolutionIdAndStudentId_returnTrue() {
        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudent(activity, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByStudentIdAndId(student.getId(), resolution.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("returns false when the student did not created the resolution")
    void existsByStudentIdAndId_givenResolutionIdAndStudentId_returnFalse() {
        User studentB = UserFactory.sampleUserStudentB();
        userRepository.save(studentB);

        Resolution resolution = ResolutionFactory.createValidResolutionWithActivityAndStudentAndExecutedSuccessStatus(activity, student);
        resolutionRepository.save(resolution);

        boolean exists = resolutionRepository.existsByStudentIdAndId(studentB.getId(), resolution.getId());

        assertFalse(exists);
    }
}
