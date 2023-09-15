package app.codeodyssey.codeodysseyapi.invitation.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Tests for Invitation Repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class InvitationRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    User professor;
    Course course;

    @BeforeEach
    void setUp() {
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);

        userRepository.save(professor);
        courseRepository.save(course);
    }

    @AfterEach
    void tearDown() {
        invitationRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns an empty list when the course doesn't have any invitations")
    void findAllByCourseId_givenCourseWithNoInvitations_returnNull() {
        Invitation invitation = invitationRepository.findByCourseId(course.getId());

        assertThat(invitation).isNull();
    }

    @Test
    @DisplayName("returns an invitation when the course has one invitation")
    void findAllByCourseId_givenCourseWithOneInvitation_returnInvitation() {
        var invitation = InvitationFactory.sampleInvitationWithCourse(course);
        invitationRepository.save(invitation);

        Invitation invitationRepositoryByCourseId = invitationRepository.findByCourseId(course.getId());

        assertThat(invitationRepositoryByCourseId).isNotNull();
        assertThat(invitationRepositoryByCourseId.getId()).isEqualTo(invitation.getId());
    }

    @Test
    @DisplayName("returns the last invitation created when the course has many invitations")
    void findAllByCourseId_givenCourseWithManyInvitations_returnList() {
        var invitationA = InvitationFactory.sampleInvitationWithCourse(course);
        invitationRepository.save(invitationA);

        var invitationB = InvitationFactory.sampleInvitationWithCourse(course);
        invitationRepository.save(invitationB);

        Invitation invitationRepositoryByCourseId = invitationRepository.findByCourseId(course.getId());
        assertThat(invitationRepositoryByCourseId).isNotNull();
        assertThat(invitationRepositoryByCourseId.getId()).isEqualTo(invitationB.getId());
    }
}
