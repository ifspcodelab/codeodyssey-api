package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import org.assertj.core.api.Assertions;
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
    private CreateUserService createUserService;

    @Autowired
    private CreateCourseService createCourseService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    private CreateUserCommand userCommand;
    private CreateCourseCommand courseCommand;

    @BeforeEach
    public void setup() {
        userCommand = new CreateUserCommand("UserName", "Email",  "Password", UserRole.PROFESSOR);
        courseCommand = new CreateCourseCommand("CourseName", "Slug",  LocalDate.now(), LocalDate.now());
    }

    @Test
    @DisplayName("return CourseResponse when given a professorId and CourseCommand")
    void execute_givenProfessorIdAndCourseCommand_returnCourseResponse() {
        UserResponse user = createUserService.execute(userCommand);
        CourseResponse course = createCourseService.execute(user.id(), courseCommand);

        Assertions.assertThat(course).isNotNull();
    }

    @Test
    @DisplayName("return ResourceNotFoundException when given a non existing professorId")
    void execute_givenExistingProfessorIdAndCourseCommand_returnException() {
        UserResponse userResponse = createUserService.execute(userCommand);
        User user = userRepository.findById(userResponse.id()).orElseThrow(() -> new ResourceNotFoundException(userResponse.id(), Resource.USER));
        Course existingCourse = new Course("CourseName", "Slug",  LocalDate.now(), LocalDate.now(), user);
        courseRepository.save(existingCourse);

        Assertions.assertThatExceptionOfType(ViolationException.class).isThrownBy(() -> createCourseService.execute(userResponse.id(), courseCommand));
    }
}
