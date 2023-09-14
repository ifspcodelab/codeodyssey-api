package app.codeodyssey.codeodysseyapi.invitation.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.api.InvitationResponse;
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
@DisplayName("Tests for Get Invitations Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetInvitationServiceTest {
    @Autowired
    GetInvitationService getInvitationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    User professor, student;
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
    @DisplayName("returns a user list when given a valid course id and and professor is logged in")
    void getInvitationsService_givenValidCourseIdAndProfessorLoggedIn_returnsList() {
        List<InvitationResponse> invitationList = getInvitationService.execute(course.getId(), professor.getEmail());
        assertThat(invitationList).isNotEmpty();
    }

    @Test
    @DisplayName("returns forbidden when given a role that is not professor")
    void getInvitationsService_givenInvalidProfessorRole_returns403Forbidden() {
        var user = UserFactory.createValidUser();
        userRepository.save(user);

        Assertions.assertThatExceptionOfType(ForbiddenAccessException.class)
                .isThrownBy(() -> getInvitationService.execute(user.getId(), user.getEmail()));
    }

    @Test
    @DisplayName("returns conflict when given an invalid course id")
    void getInvitationsService_givenInvalidCourseId_returns401Conflict() {
        UUID invalidCourseId = UUID.randomUUID();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> getInvitationService.execute(invalidCourseId, professor.getEmail()));
    }
}
