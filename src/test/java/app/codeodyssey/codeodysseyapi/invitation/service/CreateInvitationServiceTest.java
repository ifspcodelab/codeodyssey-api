package app.codeodyssey.codeodysseyapi.invitation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@DisplayName("Create Invitation Service tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CreateInvitationServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    InvitationRepository invitationRepository;

    @Autowired
    private CreateInvitationService createInvitationService;

    @AfterEach
    void afterEach() {
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("createInvitationService given professor of a course returns invite")
    void createInvitationService_givenCourseAndProfessor_returnsInvite() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var command = new InvitationCreateCommand(LocalDate.now());
        userRepository.save(professor);
        courseRepository.save(course);

        var invitation = assertDoesNotThrow(
                () -> createInvitationService.execute(command, course.getId(), professor.getEmail()));

        assertThat(invitation).isNotNull();
        assertThat(invitation).isInstanceOf(InvitationResponse.class);
        assertThat(invitation.expirationDate()).isEqualTo(command.expirationDate());
        assertThat(invitation.link()).isEqualTo("/invitations/%s".formatted(invitation.id()));
    }

    @Test
    @DisplayName("createInvitationService given non professor user returns unauthorized")
    void createInvitationService_givenNonProfessor_returnsUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var user = UserFactory.sampleUserStudent();
        var command = new InvitationCreateCommand(LocalDate.now());
        userRepository.saveAll(List.of(professor, user));
        courseRepository.save(course);

        var serviceThrowable = (RuntimeException)
                catchThrowable(() -> createInvitationService.execute(command, course.getId(), user.getEmail()));

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(ForbiddenAccessException.class);
        ForbiddenAccessException forbiddenAccessException = (ForbiddenAccessException) serviceThrowable;
        assertThat(forbiddenAccessException.getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("createInvitationService given nonexistent course returns not found")
    void createInvitationService_givenNonexistentCourse_returnsNotFound() {
        var professor = UserFactory.sampleUserProfessor();
        var command = new InvitationCreateCommand(LocalDate.now());
        var courseId = UUID.randomUUID();
        userRepository.save(professor);

        var serviceThrowable = (RuntimeException)
                catchThrowable(() -> createInvitationService.execute(command, courseId, professor.getEmail()));

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(ResourceNotFoundException.class);
        ResourceNotFoundException resourceNotFoundException = (ResourceNotFoundException) serviceThrowable;
        assertThat(resourceNotFoundException.getId()).isEqualTo(courseId);
        assertThat(resourceNotFoundException.getResource()).isEqualTo(Resource.COURSE);
    }

    @Test
    @DisplayName("createInvitationService given professor of a different course returns unauthorized")
    void createInvitationService_givenNonProfessorOfACourse_returnsUnauthorized() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var command = new InvitationCreateCommand(LocalDate.now());
        userRepository.saveAll(List.of(professor, professorB));
        courseRepository.save(course);

        var serviceThrowable = (RuntimeException)
                catchThrowable(() -> createInvitationService.execute(command, course.getId(), professorB.getEmail()));

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(ForbiddenAccessException.class);
        ForbiddenAccessException forbiddenAccessException = (ForbiddenAccessException) serviceThrowable;
        assertThat(forbiddenAccessException.getId()).isEqualTo(professorB.getId());
    }

    @Test
    @DisplayName("createInvitationService given expiration date before current date returns conflict")
    void createInvitationService_givenExpirationDateBeforeCurrentDate_returnsConflict() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var command = new InvitationCreateCommand(LocalDate.of(1000, 01, 01));
        userRepository.save(professor);
        courseRepository.save(course);

        var serviceThrowable = (RuntimeException)
                catchThrowable(() -> createInvitationService.execute(command, course.getId(), professor.getEmail()));

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(ViolationException.class);
        ViolationException violationException = (ViolationException) serviceThrowable;
        assertThat(violationException.getResource()).isEqualTo(Resource.INVITATION);
        assertThat(violationException.getType()).isEqualTo(ViolationType.INVITATION_EXPIRATION_DATE_BEFORE_TODAY);
        assertThat(violationException.getDetails()).isEqualTo(command.expirationDate().toString());
    }

    @Test
    @DisplayName("createInvitationService given expiration date after course end date returns conflict")
    void createInvitationService_givenExpirationDateAfterCourseEndDate_returnsConflict() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var command = new InvitationCreateCommand(course.getEndDate().plusDays(1));
        userRepository.save(professor);
        courseRepository.save(course);

        var serviceThrowable = (RuntimeException)
                catchThrowable(() -> createInvitationService.execute(command, course.getId(), professor.getEmail()));

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(ViolationException.class);
        ViolationException violationException = (ViolationException) serviceThrowable;
        assertThat(violationException.getResource()).isEqualTo(Resource.INVITATION);
        assertThat(violationException.getType()).isEqualTo(ViolationType.INVITATION_EXPIRATION_DATE_AFTER_COURSE_END_DATE);
        assertThat(violationException.getDetails()).isEqualTo(command.expirationDate().toString());
    }
}
