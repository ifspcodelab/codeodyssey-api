package app.codeodyssey.codeodysseyapi.course.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.common.exception.ForbiddenAccessException;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@DisplayName("Get Student's Courses Service tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class GetStudentCoursesServiceTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private GetStudentCoursesService getStudentCoursesService;

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("getStudentCoursesService returns course response list when an user is a student")
    void getStudentCoursesService_givenStudent_returnsList() {
        var user = UserFactory.sampleUserStudent();
        userRepository.save(user);

        Throwable serviceThrowable =
                catchThrowable(() -> getStudentCoursesService.execute(user.getId(), user.getEmail()));

        assertThat(serviceThrowable).doesNotThrowAnyException();
    }
}
