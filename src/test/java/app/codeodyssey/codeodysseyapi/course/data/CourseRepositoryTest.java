package app.codeodyssey.codeodysseyapi.course.data;

import static org.assertj.core.api.Assertions.assertThat;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.enrollment.util.EnrollmentFactory;
import app.codeodyssey.codeodysseyapi.invitation.util.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

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
     * case 1: returns an empty list
     * case 2: returns a list with 1 element
     * case 3: returns a list with N elements
     * case 4: returns a list with element named A before element B
     * case 5: returns a list with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName(
            "findAllByOrderByNameAscEndDateAsc() returns an empty list when the course table doesn't have any stored rows")
    void findAll_givenEmptyDatabase_returnEmptyList() {
        testEntityManager.flush();

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list when the course table has one stored row")
    void findAll_givenOneStoredRow_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(user);
        persistAllAndFlush(user, course);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
    }

    @Test
    @DisplayName("findAllByOrderByNameAscEndDateAsc() returns a list when the course table has many stored rows")
    void findAll_givenManyStoredRows_returnsList() {
        var user = UserFactory.sampleUserProfessor();
        var course1 = CourseFactory.sampleCourseWithProfessor(user);
        var course2 = CourseFactory.sampleCourseWithProfessor(user);
        var course3 = CourseFactory.sampleCourseWithProfessor(user);
        course2.setName("Spring Security");
        course2.setSlug("spring-security");
        course3.setName("Spring Cloud");
        course3.setSlug("spring-cloud");
        persistAllAndFlush(user, course1, course2, course3);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).containsExactlyInAnyOrder(course1, course2, course3);
    }

    @Test
    @DisplayName(
            "findAllByOrderByNameAscEndDateAsc() returns a list ordered by course name ascending when the course table has many stored rows")
    void findAll_givenManyStoredRows_returnsListOrderedByNameAsc() {
        var user = UserFactory.sampleUserProfessor();
        var courseA = CourseFactory.sampleCourseWithProfessor(user);
        var courseB = CourseFactory.sampleCourseWithProfessor(user);
        courseA.setName("Spring Cloud");
        courseA.setSlug("spring-cloud");
        courseB.setName("Spring Security");
        courseB.setSlug("spring-security");
        persistAllAndFlush(user, courseA, courseB);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).containsExactly(courseA, courseB);
        assertThat(courseList).extracting(Course::getName).isSorted();
    }

    @Test
    @DisplayName(
            "findAllByOrderByNameAscEndDateAsc() returns a list ordered by course name ascending and end date ascending when the course table has many stored rows")
    void findAll_givenManyStoredRows_returnsListOrderedByNameAscEndDateAsc() {
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
        persistAllAndFlush(user, courseA1, courseA2, courseB);

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).containsExactly(courseA1, courseA2, courseB);
        assertThat(courseList)
                .isSortedAccordingTo(Comparator.comparing(Course::getName).thenComparing(Course::getEndDate));
    }

    /**
     * case 1: returns an empty list of a given professor
     * case 2: returns a list with 1 element of a given professor
     * case 3: returns a list with N elements of a given professor
     * case 4: returns a course list of a given professor with element named A before element B
     * case 5: returns a course list of a given professor list with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName(
            "findAllByProfessorIdOrderByNameAscEndDateAsc() returns an empty list when a professor doesn't have any courses")
    void findAllBy_givenNoCourseOfAProfessor_returnsEmptyList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var course = CourseFactory.sampleCourseWithProfessor(professorB);
        persistAllAndFlush(professorA, professorB, course);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list when a professor has a course")
    void findAllBy_givenOneCourseOfAProfessor_returnsList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorB);
        persistAllAndFlush(professorA, professorB, courseA, courseB);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
        assertThat(courseList).contains(courseA);
        assertThat(courseList).doesNotContain(courseB);
    }

    @Test
    @DisplayName("findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list when a professor has many courses")
    void findAllBy_givenManyCoursesOfAProfessor_returnsList() {
        var professorA = UserFactory.sampleUserProfessor();
        var professorB = UserFactory.sampleUserProfessorB();
        var courseA = CourseFactory.sampleCourseWithProfessor(professorA);
        var courseB = CourseFactory.sampleCourseBWithProfessor(professorA);
        var courseC = CourseFactory.sampleCourseCWithProfessor(professorB);
        persistAllAndFlush(professorA, professorB, courseA, courseB, courseC);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).contains(courseA, courseB);
        assertThat(courseList).doesNotContain(courseC);
    }

    @Test
    @DisplayName(
            "findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending when a professor has many courses")
    void findAllBy_givenManyCoursesOfAProfessor_returnsListOrderedByNameAsc() {
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
        persistAllAndFlush(professorA, professorB, courseA, courseB, courseC);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList).doesNotContain(courseC);
        assertThat(courseList).containsExactly(courseA, courseB);
        assertThat(courseList).extracting(Course::getName).isSorted();
    }

    @Test
    @DisplayName(
            "findAllByProfessorIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending and end date ascending when a professor has many courses")
    void findAllBy_givenManyCoursesOfAProfessor_returnsListOrderedByNameAscEndDateAsc() {
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
        persistAllAndFlush(professorA, professorB, courseA, courseB, courseC, courseD);

        List<Course> courseList = courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(professorA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList).doesNotContain(courseD);
        assertThat(courseList).containsExactly(courseA, courseB, courseC);
        assertThat(courseList)
                .isSortedAccordingTo(Comparator.comparing(Course::getName).thenComparing(Course::getEndDate));
    }

    /**
     * case 1: returns an empty list of a given student
     * case 2: returns a list with 1 element of a given student
     * case 3: returns a list with N elements of a given student
     * case 4: returns a list with N elements of a given student enrolled on courses of different professors
     * case 5: returns a course list of a given student with element named A before element B
     * case 6: returns a course list of a given student with element named AA and end date january 1st before element AA january 2nd
     */
    @Test
    @DisplayName(
            "findAllByStudentIdOrderByNameAscEndDateAsc() returns an empty list when a student isn't enrolled on any courses")
    void findAllBy_givenNoEnrolledCourseOfAStudent_returnsEmptyList() {
        var professor = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(professor);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitation = InvitationFactory.sampleInvitationWithCourse(course);
        var enrollment = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        persistAllAndFlush(professor, course, studentA, studentB, invitation, enrollment);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isEmpty();
    }

    @Test
    @DisplayName("findAllByStudentIdOrderByNameAscEndDateAsc() returns a list when a student is enrolled on a course")
    void findAllBy_givenStudentEnrolledOnACourse_returnsList() {
        var professor = UserFactory.sampleUserProfessor();
        var course = CourseFactory.sampleCourseWithProfessor(professor);
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitation = InvitationFactory.sampleInvitationWithCourse(course);
        var enrollmentStudentA = EnrollmentFactory.sampleEnrollment(invitation, studentA);
        var enrollmentStudentB = EnrollmentFactory.sampleEnrollment(invitation, studentB);
        persistAllAndFlush(professor, course, studentA, studentB, invitation, enrollmentStudentA, enrollmentStudentB);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(1);
        assertThat(courseList)
                .extracting(Course::getId)
                .contains(invitation.getCourse().getId());
    }

    @Test
    @DisplayName(
            "findAllByStudentIdOrderByNameAscEndDateAsc() returns a list when a student is enrolled on many courses")
    void findAllBy_givenStudentEnrolledOnManyCourses_returnsList() {
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
        persistAllAndFlush(
                professor,
                courseA,
                courseB,
                courseC,
                studentA,
                studentB,
                invitationA,
                invitationB,
                invitationC,
                enrollmentStudentACourseA,
                enrollmentStudentACourseB,
                enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList)
                .extracting(Course::getId)
                .doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList)
                .extracting(Course::getId)
                .contains(
                        invitationA.getCourse().getId(), invitationB.getCourse().getId());
    }

    @Test
    @DisplayName(
            "findAllByStudentIdOrderByNameAscEndDateAsc() returns a list when a student is enrolled on courses of many professors")
    void findAllBy_givenStudentEnrolledOnCoursesOfManyProfessors_returnsList() {
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
        persistAllAndFlush(
                professorA,
                professorB,
                professorC,
                courseA,
                courseB,
                courseC,
                studentA,
                studentB,
                invitationA,
                invitationB,
                invitationC,
                enrollmentStudentACourseA,
                enrollmentStudentACourseB,
                enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList)
                .extracting(Course::getId)
                .doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList)
                .extracting(Course::getId)
                .contains(
                        invitationA.getCourse().getId(), invitationB.getCourse().getId());
    }

    @Test
    @DisplayName(
            "findAllByStudentIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending when a student is enrolled on many courses")
    void findAllBy_givenStudentEnrolledOnManyCourses_returnsListOrderedByName() {
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
        persistAllAndFlush(
                professorA,
                professorB,
                courseA,
                courseB,
                courseC,
                studentA,
                studentB,
                invitationA,
                invitationB,
                invitationC,
                enrollmentStudentACourseA,
                enrollmentStudentACourseB,
                enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(2);
        assertThat(courseList)
                .extracting(Course::getId)
                .doesNotContain(invitationC.getCourse().getId());
        assertThat(courseList).containsExactly(courseA, courseB);
        assertThat(courseList).extracting(Course::getName).isSorted();
    }

    @Test
    @DisplayName(
            "findAllByStudentIdOrderByNameAscEndDateAsc() returns a list ordered by course name ascending and end date ascending when a student is enrolled on many courses")
    void findAllBy_givenStudentEnrolledOnManyCourses_returnsListOrderedByNameAscEndDateAsc() {
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
        var studentA = UserFactory.sampleUserStudent();
        var studentB = UserFactory.sampleUserStudentB();
        var invitationA = InvitationFactory.sampleInvitationWithCourse(courseA);
        var invitationB = InvitationFactory.sampleInvitationWithCourse(courseB);
        var invitationC = InvitationFactory.sampleInvitationWithCourse(courseC);
        var invitationD = InvitationFactory.sampleInvitationWithCourse(courseD);
        var enrollmentStudentACourseA = EnrollmentFactory.sampleEnrollment(invitationA, studentA);
        var enrollmentStudentACourseB = EnrollmentFactory.sampleEnrollment(invitationB, studentA);
        var enrollmentStudentACourseC = EnrollmentFactory.sampleEnrollment(invitationC, studentA);
        var enrollmentStudentBCourseC = EnrollmentFactory.sampleEnrollment(invitationD, studentB);
        persistAllAndFlush(
                professorA,
                professorB,
                courseA,
                courseB,
                courseC,
                courseD,
                studentA,
                studentB,
                invitationA,
                invitationB,
                invitationC,
                invitationD,
                enrollmentStudentACourseA,
                enrollmentStudentACourseB,
                enrollmentStudentACourseC,
                enrollmentStudentBCourseC);

        List<Course> courseList = courseRepository.findAllByStudentIdOrderByNameAscEndDateAsc(studentA.getId());

        assertThat(courseList).isNotEmpty();
        assertThat(courseList).hasSize(3);
        assertThat(courseList)
                .extracting(Course::getId)
                .doesNotContain(invitationD.getCourse().getId());
        assertThat(courseList).containsExactly(courseA, courseB, courseC);
        assertThat(courseList)
                .isSortedAccordingTo(Comparator.comparing(Course::getName).thenComparing(Course::getEndDate));
    }

    @Test
    @DisplayName("returns true when given a existing slug and professor")
    void existsBySlugAndProfessor_givenExistingSlugAndProfessor_returnTrue() {
        var user = UserFactory.createValidProfessor();
        var course = CourseFactory.createValidCourseWithProfessor(user);

        testEntityManager.persist(user);
        testEntityManager.persist(course);
        testEntityManager.flush();
        boolean exists = courseRepository.existsBySlugAndProfessor(course.getSlug(), user);
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("returns false when given a non existing slug and professor")
    void existsBySlugAndProfessor_givenNonExistingSlugAndProfessor_returnFalse() {
        var user = UserFactory.createValidProfessor();
        var course = CourseFactory.createValidCourseWithProfessor(user);

        testEntityManager.persist(user);
        testEntityManager.persist(course);
        testEntityManager.flush();
        boolean exists = courseRepository.existsBySlugAndProfessor(
                "non-existing-slug", new User("UserName", "non-existing-email", "Password"));
        Assertions.assertFalse(exists);
    }

    private void persistAllAndFlush(Object... entities) {
        for (Object entity : entities) {
            testEntityManager.persistAndFlush(entity);
        }
    }
}
