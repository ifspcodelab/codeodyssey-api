package app.codeodyssey.codeodysseyapi.activity.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("Activity Repository tests")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void beforeEach() {
        activityRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("persists an activity when successful")
    void persistsActivity_whenSuccessful() {

        var course = CourseFactory.sampleCourse();
        var professor = course.getProfessor();
        var activity = ActivityFactory.sampleActivity(course);

        var expectedProfessor = this.userRepository.save(professor);
        var expectedCourse = this.courseRepository.save(course);
        var expectedActivity = this.activityRepository.save(activity);

        assertThat(expectedProfessor).isNotNull();
        assertThat(expectedCourse).isNotNull();
        assertThat(expectedActivity).isNotNull();

        assertThat(expectedActivity.getCourse().getId()).isEqualTo(expectedCourse.getId());

    }

}
