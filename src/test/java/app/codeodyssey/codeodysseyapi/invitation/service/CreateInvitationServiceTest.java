package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.UnauthorizedAccessException;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
    @DisplayName("createInvitationService given professor returns invite")
    void createInvitationService_givenProfessor_returnsInvite() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var command = new InvitationCreateCommand(LocalDate.now());
        userRepository.save(professor);
        courseRepository.save(course);

        var invitation = assertDoesNotThrow(() ->
                createInvitationService.execute(command, course.getId(), professor.getEmail())
        );

        assertThat(invitation).isNotNull();
        assertThat(invitation).isInstanceOf(InvitationResponse.class);
        assertThat(invitation.expirationDate()).isEqualTo(command.expirationDate());
        assertThat(invitation.link()).isEqualTo("/invites/%s".formatted(invitation.id()));
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

        var serviceThrowable = (RuntimeException) catchThrowable(() ->
                createInvitationService.execute(command, course.getId(), user.getEmail())
        );

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(UnauthorizedAccessException.class);
        UnauthorizedAccessException unauthorizedAccessException = (UnauthorizedAccessException) serviceThrowable;
        assertThat(unauthorizedAccessException.getId()).isEqualTo(user.getId());
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

        var serviceThrowable = (RuntimeException) catchThrowable(() ->
                createInvitationService.execute(command, course.getId(), professorB.getEmail())
        );

        assertThat(serviceThrowable).isNotNull();
        assertThat(serviceThrowable).isInstanceOf(UnauthorizedAccessException.class);
        UnauthorizedAccessException unauthorizedAccessException = (UnauthorizedAccessException) serviceThrowable;
        assertThat(unauthorizedAccessException.getId()).isEqualTo(professorB.getId());
    }
}
