package app.codeodyssey.codeodysseyapi.enrollment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.StudentAlreadyEnrolledException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@DisplayName("Create Enrollment Service tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
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
    void createEnrollmentService_givenStudentAndInvitation_returnsEnrollment() {
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
        assertThat(enrollment.invitation().getId()).isEqualTo(invitation.getId());
        assertThat(enrollment.student().getId()).isEqualTo(student.getId());
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
}
