package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
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

import java.time.LocalDate;

@SpringBootTest
@DisplayName("Tests for Course Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CreateCourseServiceTest {
    @Autowired
    private CreateCourseService createCourseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private CreateCourseCommand courseCommand;

    @BeforeEach
    public void setup() {
        courseRepository.deleteAll();
        userRepository.deleteAll();

        courseCommand = new CreateCourseCommand("CourseName", "Slug",  LocalDate.now(), LocalDate.now());
    }

    @AfterEach
    public void tearDown() {
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns CourseResponse when given a professorId and CourseCommand")
    void execute_givenProfessorIdAndCourseCommand_returnCourseResponse() {
        User professor = UserFactory.createValidProfessor();
        userRepository.save(professor);

        CourseResponse course = createCourseService.execute(professor.getId(), courseCommand);

        Assertions.assertThat(course).isNotNull();
    }

    @Test
    @DisplayName("returns ViolationException when given a existing professorId and CourseCommand")
    void execute_givenExistingProfessorIdAndCourseCommand_returnException() {
        User professor = UserFactory.createValidProfessor();
        userRepository.save(professor);

        Course existingCourse = new Course("CourseName", "Slug",  LocalDate.now(), LocalDate.now(), professor);
        courseRepository.save(existingCourse);

        Assertions.assertThatExceptionOfType(ViolationException.class).isThrownBy(() -> createCourseService.execute(professor.getId(), courseCommand));
    }
}
