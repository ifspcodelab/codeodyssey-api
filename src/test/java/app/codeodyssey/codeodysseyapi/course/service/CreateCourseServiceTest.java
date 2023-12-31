package app.codeodyssey.codeodysseyapi.course.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@DisplayName("Tests for Create Course Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CreateCourseServiceTest {
    @Autowired
    private CreateCourseService createCourseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    User professor;
    CreateCourseCommand courseCommand;

    @BeforeEach
    public void setup() {
        professor = UserFactory.createValidProfessor();
        userRepository.save(professor);

        courseCommand = new CreateCourseCommand("CourseName", "Slug", LocalDate.now(), LocalDate.now());
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns CourseResponse when given a valid professorId and CourseCommand")
    void execute_givenValidProfessorIdAndCourseCommand_returnCourseResponse() {
        var course = assertDoesNotThrow(() -> createCourseService.execute(professor.getId(), courseCommand));

        assertThat(course).isNotNull();
        assertThat(course).isInstanceOf(CourseResponse.class);
    }

    @Test
    @DisplayName("returns not found when given an invalid user id")
    void execute_givenInvalidUserId_return404NotFound() {
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> createCourseService.execute(UUID.randomUUID(), courseCommand));
    }

    @Test
    @DisplayName("returns forbidden when given a role that is not professor")
    void execute_givenInvalidProfessorRole_returns403Forbidden() {
        var user = UserFactory.createValidUser();
        userRepository.save(user);

        assertThatExceptionOfType(ForbiddenAccessException.class)
                .isThrownBy(() -> createCourseService.execute(user.getId(), courseCommand));
    }

    @Test
    @DisplayName("returns conflict when given an existing CourseCommand")
    void execute_givenExistingCourseCommand_return409Conflict() {
        var existingCourse = new Course("CourseName", "Slug", LocalDate.now(), LocalDate.now(), professor);
        courseRepository.save(existingCourse);

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createCourseService.execute(professor.getId(), courseCommand));
    }

    @Test
    @DisplayName("returns conflict when the course start date is before the current date")
    void execute_givenStartDateBeforeCurrentDate_return409Conflict() {
        courseCommand = new CreateCourseCommand("CourseName", "Slug", LocalDate.of(1000, 01, 01), LocalDate.now());

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createCourseService.execute(professor.getId(), courseCommand));
    }

    @Test
    @DisplayName("returns conflict when the course end date is before the start date")
    void execute_givenEndDateBeforeStartDate_return409Conflict() {
        courseCommand = new CreateCourseCommand("CourseName", "Slug", LocalDate.now(), LocalDate.of(1000, 01, 01));

        assertThatExceptionOfType(ViolationException.class)
                .isThrownBy(() -> createCourseService.execute(professor.getId(), courseCommand));
    }
}
