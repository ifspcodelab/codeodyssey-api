package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.utils.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.utils.EnrollmentFactory;
import app.codeodyssey.codeodysseyapi.invitation.utils.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.utils.UserFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("Course Repository tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CourseRepository courseRepository;

    /**
     * TODO: findAllOrderByNameAscEndDateAsc();
     * case 1: returns an empty list
     * case 2: returns a list with 1 element
     * case 3: returns a list with N elements
     * case 4: returns a list with element named A before element B
     * case 5: returns a list with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns an empty list when the course table doesn't have any stored rows")
    void findAllByOrderByNameAscEndDateAsc_givenEmptyDatabase_returnEmptyList() {
        testEntityManager.flush();

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();
        System.out.println(courseList.toString());

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list when the course table has one stored row")
    void findAllByOrderByNameAscEndDateAsc_givenOneStoredRow_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(user);
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(course);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list when the course table has many stored rows")
    void findAllByOrderByNameAscEndDateAsc_givenManyStoredRows_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course1 = CourseFactory.sampleCourseWithProfessor(user);
        var course2 = CourseFactory.sampleCourseWithProfessor(user);
        var course3 = CourseFactory.sampleCourseWithProfessor(user);
        course2.setName("Spring Security");
        course2.setSlug("spring-security");
        course3.setName("Spring Cloud");
        course3.setSlug("spring-cloud");
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(course1);
        testEntityManager.persistAndFlush(course2);
        testEntityManager.persistAndFlush(course3);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).containsExactlyInAnyOrder(course1, course2, course3);
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list ordered by course name ascending when the course table has many stored rows")
    void findAllByOrderByNameAscEndDateAsc_givenManyStoredRows_returnsListOrderedByNameAsc() {
        var user = UserFactory.sampleUserProfessor();
        var courseA = CourseFactory.sampleCourseWithProfessor(user);
        var courseB = CourseFactory.sampleCourseWithProfessor(user);
        courseA.setName("Spring Cloud");
        courseA.setSlug("spring-cloud");
        courseB.setName("Spring Security");
        courseB.setSlug("spring-security");
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseA);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).containsExactly(courseA, courseB);
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list ordered by course name ascending and end date ascending when the course table has many stored rows")
    void findAllByOrderByNameAscEndDateAsc_givenManyStoredRows_returnsListOrderedByNameAscEndDateAsc() {
        var user = UserFactory.sampleUserProfessor();
        var courseA1 = CourseFactory.sampleCourseWithProfessor(user);
        var courseA2 = CourseFactory.sampleCourseWithProfessor(user);
        var courseB = CourseFactory.sampleCourseWithProfessor(user);
        courseA1.setName("Spring Cloud");
        courseA1.setSlug("spring-cloud");
        courseA1.setEndDate(LocalDate.of(2023, 7, 17));
        courseA2.setName("Spring Cloud");
        courseA2.setSlug("spring-cloud-second-class");
        courseA2.setEndDate(LocalDate.of(2023, 7, 18));
        courseB.setName("Spring Security");
        courseB.setSlug("spring-security");
        courseB.setEndDate(LocalDate.of(2023, 7, 17));
        testEntityManager.persistAndFlush(user);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseA2);
        testEntityManager.persistAndFlush(courseA1);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();
        System.out.println(courseList.toString());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).containsExactly(courseA1, courseA2, courseB);
    }

    /**
     * TODO: findAllByProfessorIdOrderByNameAscEndDateAsc();
     * case 1: returns an empty list of a given professor
     * case 2: returns a list with 1 element of a given professor
     * case 3: returns a list with N elements of a given professor
     * case 4: returns a course list of a given professor with element named A before element B
     * case 5: returns a course list of a given professor list with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc returns an empty list when a professor doesn't have any courses")
    void findAllByProfessorIdOrderByNameAscEndDateAsc_givenNoCourseOfAProfessor_returnsEmptyList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var course = CourseFactory.sampleCourseWithProfessor(professorB);
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(course);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc returns a list when a professor has a course")
    void findAllByProfessorIdOrderByNameAscEndDateAsc_givenOneCourseOfAProfessor_returnsList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorB);
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(courseA);
        testEntityManager.persistAndFlush(courseB);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
        assertThat(courseList).contains(courseA);
        assertThat(courseList).doesNotContain(courseB);
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc returns a list when a professor has many courses")
    void findAllByProfessorIdOrderByNameAscEndDateAsc_givenManyCoursesOfAProfessor_returnsList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorA);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professorB);
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(courseA);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).contains(courseA, courseB);
        assertThat(courseList).doesNotContain(courseC);
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending when a professor has many courses")
    void findAllByProfessorIdOrderByNameAscEndDateAsc_givenManyCoursesOfAProfessor_returnsListOrderedByNameAsc() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorA);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professorB);
        courseA.setName("Spring Cloud");
        courseA.setSlug("spring-cloud");
        courseB.setName("Spring Security");
        courseB.setSlug("spring-security");
        courseC.setName("Spring MVC");
        courseC.setSlug("spring-mvc");
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);
        testEntityManager.persistAndFlush(courseA);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).doesNotContain(courseC);
        assertThat(courseList).containsExactly(courseA, courseB);
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending and end date ascending when a professor has many courses")
    void findAllByProfessorIdOrderByNameAscEndDateAsc_givenManyCoursesOfAProfessor_returnsListOrderedByNameAscEndDateAsc() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseC = CourseFactory.sampleCourseBWithProfessor(professorA);
        var courseD = CourseFactory.sampleCourseCWithProfessor(professorB);
        courseA.setName("Spring Cloud");
        courseA.setSlug("spring-cloud");
        courseA.setEndDate(LocalDate.of(2023, 7, 6));
        courseB.setName("Spring Cloud");
        courseB.setSlug("spring-cloud-second-class");
        courseB.setEndDate(LocalDate.of(2023, 7, 7));
        courseC.setName("Spring MVC");
        courseC.setSlug("spring-mvc");
        courseC.setEndDate(LocalDate.of(2023, 7, 7));
        courseD.setName("Spring Security");
        courseD.setSlug("spring-security");
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);
        testEntityManager.persistAndFlush(courseD);
        testEntityManager.persistAndFlush(courseA);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).doesNotContain(courseD);
        assertThat(courseList).containsExactly(courseA, courseB, courseC);
    }

    /**
     * TODO: findAllByStudentIdOrderByNameAscEndDateAsc();
     * case 1: returns an empty list of a given student
     * case 2: returns a list with 1 element of a given student
     * case 3: returns a list with N elements of a given student
     * case 4: returns a list with N elements of a given student enrolled on courses of different professors
     * case 5: returns a course list of a given student with element named A before element B
     * case 6: returns a course list of a given student with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc returns an empty list when a student isn't enrolled on any courses")
    void findAllByStudentIdOrderByNameAscEndDateAsc_givenNoEnrolledCourseOfAStudent_returnsEmptyList() {
        var professor = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(professor);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitation = InvitationFactory.sampleInvitationWithCourse(course);
        var enrollment = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        testEntityManager.persistAndFlush(professor);
        testEntityManager.persistAndFlush(course);
        testEntityManager.persistAndFlush(studentA);
        testEntityManager.persistAndFlush(studentB);
        testEntityManager.persistAndFlush(invitation);
        testEntityManager.persistAndFlush(enrollment);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc returns a list when a student is enrolled on a course")
    void findAllByStudentIdOrderByNameAscEndDateAsc_givenStudentEnrolledOnACourse_returnsList() {
        var professor = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(professor);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitation = InvitationFactory.sampleInvitationWithCourse(course);
        var enrollmentStudentA = EnrollmentFactory.sampleEnrollment(invitation, studentA);
        var enrollmentStudentB = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        testEntityManager.persistAndFlush(professor);
        testEntityManager.persistAndFlush(course);
        testEntityManager.persistAndFlush(studentA);
        testEntityManager.persistAndFlush(studentB);
        testEntityManager.persistAndFlush(invitation);
        testEntityManager.persistAndFlush(enrollmentStudentA);
        testEntityManager.persistAndFlush(enrollmentStudentB);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
        assertThat(courseList).extracting(Course::getId).contains(invitation.getCourse().getId());
    }

    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc returns a list when a student is enrolled on many courses")
    void findAllByStudentIdOrderByNameAscEndDateAsc_givenStudentEnrolledOnManyCourses_returnsList() {
        var professor = UserFactory.sampleUserProfessor();
        var courseA = CourseFactory.sampleCourseWithProfessor(professor);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professor);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitationA = InvitationFactory.sampleInvitationWithCourse(courseA);
        var invitationB = InvitationFactory.sampleInvitationWithCourse(courseB);
        var invitationC = InvitationFactory.sampleInvitationWithCourse(courseC);
        var enrollmentStudentACourseA = EnrollmentFactory.sampleEnrollment(invitationA, studentA);
        var enrollmentStudentACourseB = EnrollmentFactory.sampleEnrollment(invitationB, studentA);
        var enrollmentStudentBCourseC = EnrollmentFactory.sampleEnrollment(invitationC, studentB);
        testEntityManager.persistAndFlush(professor);
        testEntityManager.persistAndFlush(courseA);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);
        testEntityManager.persistAndFlush(studentA);
        testEntityManager.persistAndFlush(studentB);
        testEntityManager.persistAndFlush(invitationA);
        testEntityManager.persistAndFlush(invitationB);
        testEntityManager.persistAndFlush(invitationC);
        testEntityManager.persistAndFlush(enrollmentStudentACourseA);
        testEntityManager.persistAndFlush(enrollmentStudentACourseB);
        testEntityManager.persistAndFlush(enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).extracting(Course::getId).doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList).extracting(Course::getId).contains(
                invitationA.getCourse().getId(),
                invitationB.getCourse().getId()
        );
    }

    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc returns a list when a student is enrolled on courses of many professors")
    void findAllByStudentIdOrderByNameAscEndDateAsc_givenStudentEnrolledOnCoursesOfManyProfessors_returnsList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var professorC = UserFactory.sampleUserProfessorC();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorB);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professorC);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitationA = InvitationFactory.sampleInvitationWithCourse(courseA);
        var invitationB = InvitationFactory.sampleInvitationWithCourse(courseB);
        var invitationC = InvitationFactory.sampleInvitationWithCourse(courseC);
        var enrollmentStudentACourseA = EnrollmentFactory.sampleEnrollment(invitationA, studentA);
        var enrollmentStudentACourseB = EnrollmentFactory.sampleEnrollment(invitationB, studentA);
        var enrollmentStudentBCourseC = EnrollmentFactory.sampleEnrollment(invitationC, studentB);
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(professorC);
        testEntityManager.persistAndFlush(courseA);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);
        testEntityManager.persistAndFlush(studentA);
        testEntityManager.persistAndFlush(studentB);
        testEntityManager.persistAndFlush(invitationA);
        testEntityManager.persistAndFlush(invitationB);
        testEntityManager.persistAndFlush(invitationC);
        testEntityManager.persistAndFlush(enrollmentStudentACourseA);
        testEntityManager.persistAndFlush(enrollmentStudentACourseB);
        testEntityManager.persistAndFlush(enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).extracting(Course::getId).doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList).extracting(Course::getId).contains(
                invitationA.getCourse().getId(),
                invitationB.getCourse().getId()
        );
    }

    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc returns a list ordered by course name ascending")
    void findAllByStudentIdOrderByNameAscEndDateAsc_givenStudentEnrolledOnManyCourses_returnsListOrderedByName() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorA);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professorB);
        courseA.setName("Spring Cloud");
        courseA.setSlug("spring-cloud");
        courseB.setName("Spring Security");
        courseB.setSlug("spring-security");
        courseC.setName("Spring MVC");
        courseC.setSlug("spring-mvc");
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitationA = InvitationFactory.sampleInvitationWithCourse(courseA);
        var invitationB = InvitationFactory.sampleInvitationWithCourse(courseB);
        var invitationC = InvitationFactory.sampleInvitationWithCourse(courseC);
        var enrollmentStudentACourseA = EnrollmentFactory.sampleEnrollment(invitationA, studentA);
        var enrollmentStudentACourseB = EnrollmentFactory.sampleEnrollment(invitationB, studentA);
        var enrollmentStudentBCourseC = EnrollmentFactory.sampleEnrollment(invitationC, studentB);
        testEntityManager.persistAndFlush(professorA);
        testEntityManager.persistAndFlush(professorB);
        testEntityManager.persistAndFlush(courseB);
        testEntityManager.persistAndFlush(courseC);
        testEntityManager.persistAndFlush(courseA);
        testEntityManager.persistAndFlush(studentA);
        testEntityManager.persistAndFlush(studentB);
        testEntityManager.persistAndFlush(invitationA);
        testEntityManager.persistAndFlush(invitationB);
        testEntityManager.persistAndFlush(invitationC);
        testEntityManager.persistAndFlush(enrollmentStudentACourseA);
        testEntityManager.persistAndFlush(enrollmentStudentACourseB);
        testEntityManager.persistAndFlush(enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).extracting(Course::getId).doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList).containsExactly(courseA, courseB);
    }
}
