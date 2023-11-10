package app.codeodyssey.codeodysseyapi.enrollment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import app.codeodyssey.codeodysseyapi.enrollment.util.EnrollmentFactory;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@DisplayName("Create Enrollment Service tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@ExtendWith({OutputCaptureExtension.class})
public class CreateEnrollmentServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    CreateEnrollmentService createEnrollmentService;

    @AfterEach
    void afterEach() {
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createEnrollmentService given student and invitation returns enrollment")
    void createEnrollmentService_givenStudentAndInvitation_returnsEnrollment(CapturedOutput output) {
        var student = UserFactory.sampleUserStudent();
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now().plusMonths(1), course);
        userRepository.saveAll(List.of(student, professor));
        courseRepository.save(course);
        invitationRepository.save(invitation);

        var enrollment =
                assertDoesNotThrow(() -> createEnrollmentService.execute(invitation.getId(), student.getEmail()));

        assertThat(enrollment).isNotNull();
        assertThat(enrollment).isInstanceOf(EnrollmentResponse.class);
        assertThat(enrollment.invitation().id()).isEqualTo(invitation.getId());
        assertThat(enrollment.student().id()).isEqualTo(student.getId());
        assertTrue(output.toString().contains("Invitation accepted by student with id " + student.getId() + " from course with id " + invitation.getCourse().getId()));
    }

    @Test
    @DisplayName("createEnrollmentService given student and invitation returns violation exception")
    void createEnrollmentService_givenStudentAndInvitation_returnsViolationException() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now().plusMonths(1), course);
        var enrollment = EnrollmentFactory.sampleEnrollment(invitation, professor);
        userRepository.save(professor);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);

        var exception = (RuntimeException)
                catchThrowable(() -> createEnrollmentService.execute(invitation.getId(), professor.getEmail()));

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(ViolationException.class);
        ViolationException violationException = (ViolationException) exception;
        assertThat(violationException.getResource()).isEqualTo(Resource.ENROLLMENT);
        assertThat(violationException.getType()).isEqualTo(ViolationType.ENROLLMENT_PROFESSOR_WHO_CREATED_COURSE_CANNOT_BE_ENROLLED);
        assertThat(violationException.getDetails()).isEqualTo(professor.getEmail());
    }

    @Test
    @DisplayName("createEnrollmentService given student and invitation returns StudentAlreadyEnrolled exception")
    void createEnrollmentService_givenStudentAndInvitation_returnsAlreadyEnrolled() {
        var student = UserFactory.sampleUserStudent();
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now().plusMonths(1), course);
        var enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);
        userRepository.saveAll(List.of(student, professor));
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);

        var exception = (RuntimeException)
                catchThrowable(() -> createEnrollmentService.execute(invitation.getId(), student.getEmail()));

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(StudentAlreadyEnrolledException.class);
        StudentAlreadyEnrolledException alreadyEnrolledException = (StudentAlreadyEnrolledException) exception;
        assertThat(alreadyEnrolledException.getStudentId()).isEqualTo(student.getId());
        assertThat(alreadyEnrolledException.getCourseId()).isEqualTo(course.getId());
    }

    @Test
    @DisplayName("createEnrollmentService given student and invitation returns InvitationLinkExpired exception")
    void createEnrollmentService_givenStudentAndInvitation_returnsInvitationLinkExpired() {
        var student = UserFactory.sampleUserStudent();
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now(), course);
        invitation.setExpirationDate(LocalDate.now().minusMonths(1));
        userRepository.saveAll(List.of(student, professor));
        courseRepository.save(course);
        invitationRepository.save(invitation);

        var exception = (RuntimeException)
                catchThrowable(() -> createEnrollmentService.execute(invitation.getId(), student.getEmail()));

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(InvitationLinkExpiredException.class);
        InvitationLinkExpiredException invitationLinkExpiredException = (InvitationLinkExpiredException) exception;
        assertThat(invitationLinkExpiredException.getInvitationId()).isEqualTo(invitation.getId());
        assertThat(invitationLinkExpiredException.getCourseId()).isEqualTo(course.getId());
    }

    @Test
    @DisplayName("createEnrollmentService given an inactive invitation link returns InvitationLinkExpired exception")
    void createEnrollmentService_givenInvitationLinkInactive_returnsInvitationLinkExpired() {
        var student = UserFactory.sampleUserStudent();
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now(), course);
        invitation.setActive(false);
        userRepository.saveAll(List.of(student, professor));
        courseRepository.save(course);
        invitationRepository.save(invitation);

        var exception = (RuntimeException)
                catchThrowable(() -> createEnrollmentService.execute(invitation.getId(), student.getEmail()));

        assertThat(exception).isNotNull();
        assertThat(exception).isInstanceOf(InvitationLinkExpiredException.class);
        InvitationLinkExpiredException invitationLinkExpiredException = (InvitationLinkExpiredException) exception;
        assertThat(invitationLinkExpiredException.getInvitationId()).isEqualTo(invitation.getId());
        assertThat(invitationLinkExpiredException.getCourseId()).isEqualTo(course.getId());
    }
}
