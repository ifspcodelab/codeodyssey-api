package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Tests for Get Course Students Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetCourseStudentsServiceTest {
    @Autowired
    GetCourseStudentsService getCourseStudentsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    User professor, student;
    Course course;
    Invitation invitation;
    Enrollment enrollment;

    @BeforeEach
    void setUp() {
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);
        student = UserFactory.sampleUserStudent();
        enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);

        userRepository.save(professor);
        userRepository.save(student);
        courseRepository.save(course);
        invitationRepository.save(invitation);
        enrollmentRepository.save(enrollment);
    }

    @AfterEach
    void tearDown() {
        enrollmentRepository.deleteAll();
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns a user list when given a valid professor id and course slug")
    void getCourseStudentsService_givenProfessorIdAndSlug_returnsList() {
        List<User> userList = userRepository.findUsersByCourseIdOrderByName(course.getId());
        assertThat(userList).isNotEmpty();
    }

    @Test
    @DisplayName("returns exception when given a an invalid user id")
    void getCourseStudentsService_givenInvalidUserId_returns404NotFound() {
        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> getCourseStudentsService.execute(UUID.randomUUID(), course.getSlug()));
    }

    @Test
    @DisplayName("returns exception when given a an invalid professor role")
    void getCourseStudentsService_givenInvalidProfessorRole_returns403Forbidden() {
        var user = UserFactory.createValidUser();
        userRepository.save(user);

        Assertions.assertThatExceptionOfType(ForbiddenAccessException.class)
                .isThrownBy(() -> getCourseStudentsService.execute(user.getId(), course.getSlug()));
    }

    @Test
    @DisplayName("returns exception when given a an invalid professor id and course slug")
    void getCourseStudentsService_givenInvalidSlugAndProfessorId_returns401Conflict() {
        var professorB = UserFactory.sampleUserProfessorB();
        userRepository.save(professorB);

        Assertions.assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> getCourseStudentsService.execute(professorB.getId(), course.getSlug()));
    }
}
