package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.invitation.data.InvitationRepository;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("Tests for Invitation Inactive Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@ExtendWith({OutputCaptureExtension.class})
public class InvitationInactiveServiceTest {
    @Autowired
    private InvitationInactiveService invitationInactiveService;

    @Autowired
    GetInvitationService getInvitationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    User professor;
    Course course;
    Invitation invitation;

    @BeforeEach
    void setUp() {
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        invitation = InvitationFactory.sampleInvitationWithCourse(course);

        userRepository.save(professor);
        courseRepository.save(course);
        invitationRepository.save(invitation);
    }

    @AfterEach
    void tearDown() {
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("do not set invitation as inactive from a course that is valid")
    void inactiveInvitation_givenInvitationFromValidCourse_returnsActiveInvitation(CapturedOutput output) {
        course.setEndDate(LocalDate.now().plusDays(1));
        courseRepository.save(course);

        invitationInactiveService.inactiveInvitation();

        assertFalse(output.toString().contains("Invitation with id " + invitation.getId() + " set to deactivated due to end of course"));
    }

    @Test
    @DisplayName("set invitation from a course that ended as inactive")
    void inactiveInvitation_givenInvitationFromEndedCourse_returnsInactive(CapturedOutput output) {
        course.setEndDate(LocalDate.now().minusDays(1));
        courseRepository.save(course);

        invitationInactiveService.inactiveInvitation();

        assertTrue(output.toString().contains("Invitation with id " + invitation.getId() + " set to deactivated due to end of course"));
    }

    @Test
    @DisplayName("set invitations from a course that ended as inactive")
    void inactiveInvitation_givenInvitationsFromEndedCourse_returnsInactive(CapturedOutput output) {
        Invitation invitationB = InvitationFactory.sampleInvitation(LocalDate.now(), course);
        invitationRepository.save(invitationB);

        course.setEndDate(LocalDate.now().minusDays(1));
        courseRepository.save(course);

        invitationInactiveService.inactiveInvitation();

        assertTrue(output.toString().contains("Invitation with id " + invitation.getId() + " set to deactivated due to end of course"));
        assertTrue(output.toString().contains("Invitation with id " + invitationB.getId() + " set to deactivated due to end of course"));
    }

}
