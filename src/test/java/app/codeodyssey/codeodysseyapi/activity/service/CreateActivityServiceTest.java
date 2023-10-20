package app.codeodyssey.codeodysseyapi.activity.service;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.api.ActivityRequest;
import app.codeodyssey.codeodysseyapi.activity.api.ActivityResponse;
import app.codeodyssey.codeodysseyapi.activity.data.ActivityRepository;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Tests for Create Activity Service")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CreateActivityServiceTest {

    @Autowired
    private CreateActivityService createActivityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ActivityRepository activityRepository;

    User professor, invalidProfessor;
    Course course;
    ActivityRequest validRequest, invalidRequest;


    @BeforeEach
    void beforeEach() {
        course = CourseFactory.sampleCourse();
        professor = course.getProfessor();
        validRequest = new ActivityRequest(
                "title",
                "description",
                Instant.now(),
                Instant.now().plusSeconds(600),
                "test",
                "test",
                "test",
                ".java"
        );
        invalidRequest = new ActivityRequest(
                "",
                "",
                Instant.now(),
                Instant.now().plusSeconds(600),
                "test",
                "test",
                "test",
                ".java"
        );
        invalidProfessor = UserFactory.sampleUserProfessorB();

        userRepository.save(professor);
        userRepository.save(invalidProfessor);
        courseRepository.save(course);
    }

    @AfterEach
    void afterEach() {
        activityRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns ActivityResponse when given a valid course id and authenticated user is the course professor")
    void createActivity_givenAValidCourseId_returnsActivityResponse() {

        Authentication authentication = new UsernamePasswordAuthenticationToken(professor, professor.getPassword());
        var response = assertDoesNotThrow(() ->
                createActivityService.execute(course.getId(),
                validRequest,
                authentication));


        assertThat(response).isNotNull();
        assertThat(response).isInstanceOf(ActivityResponse.class);
    }

    @Test
    @DisplayName("returns ProblemDetail when user is not the course professor")
    void createActivity_givenAnInvalidUser_returnsProblemDetail() {

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(invalidProfessor, invalidProfessor.getPassword());

        assertThatExceptionOfType(ForbiddenAccessException.class)
                .isThrownBy(() -> createActivityService.execute(course.getId(), validRequest, authentication));

    }

    @Test
    @DisplayName("returns ProblemDetail when given an invalid course id")
    void createActivity_givenAnInvalidCourseId_returnsProblemDetail() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(professor, professor.getPassword());

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> createActivityService.execute(UUID.randomUUID(), invalidRequest, authentication));

    }

}
