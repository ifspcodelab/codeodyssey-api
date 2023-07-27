package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
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
}
