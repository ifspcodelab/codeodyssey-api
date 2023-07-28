package app.codeodyssey.codeodysseyapi.enrollment.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.util.EnrollmentFactory;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@DisplayName("Enrollment Repository tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class EnrollmentRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    @DisplayName("existsByStudentIdAndCourseId returns true when an enrollment exists on given course")
    void existsBy_givenStudentIdAndCourseId_returnsTrue() {
        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var invitation = InvitationFactory.sampleInvitation(LocalDate.now().plusMonths(1), course);
        var student = UserFactory.sampleUserStudent();
        var enrollment = EnrollmentFactory.sampleEnrollment(invitation, student);
        persistAllAndFlush(professor, course, invitation, student, enrollment);

        var exists = assertDoesNotThrow(
                () -> enrollmentRepository.existsByStudentIdAndInvitation_Course_Id(student.getId(), course.getId()));

        assertThat(exists).isNotNull();
        assertThat(exists).isTrue();
    }

    private void persistAllAndFlush(Object... entities) {
        for (Object entity : entities) {
            testEntityManager.persistAndFlush(entity);
        }
    }
}
