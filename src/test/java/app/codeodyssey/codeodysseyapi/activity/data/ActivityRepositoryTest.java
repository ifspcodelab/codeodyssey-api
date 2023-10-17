package app.codeodyssey.codeodysseyapi.activity.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
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
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DisplayName("Tests for Invitation Repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
@Testcontainers
public class ActivityRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ActivityRepository activityRepository;

    User professor;
    Course course;
    Activity activity;

    @BeforeEach
    void beforeEach() {
        professor = UserFactory.createValidProfessor();
        course = CourseFactory.createValidCourseWithProfessor(professor);
        activity = ActivityFactory.createValidActivityWithCourse(course);

        userRepository.save(professor);
        courseRepository.save(course);
        activityRepository.save(activity);
    }

    @AfterEach
    void tearDown() {
        activityRepository.deleteAll();
        courseRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("returns true when given an course id and activity id")
    void existsByCourseIdAndId_givenCourseIdAndActivityId_returnsTrue() {
        boolean exists = activityRepository.existsByCourseIdAndId(course.getId(), activity.getId());

        assertTrue(exists);
    }

    @Test
    @DisplayName("returns false when given an invalid course id")
    void existsByCourseIdAndId_givenInvalidCourseId_returnsFalse() {
        var courseB = CourseFactory.sampleCourseBWithProfessor(professor);
        courseRepository.save(courseB);

        boolean exists = activityRepository.existsByCourseIdAndId(courseB.getId(), activity.getId());

        assertFalse(exists);
    }

    @Test
    @DisplayName("returns empty when given an course id and activity id")
    void findByCourseIdAndActivityId_givenCourseIdAndActivityId_returnsNull() {
        Activity activityRepositoryByCourseIdAndActivityId = activityRepository.findByCourseIdAndActivityId(course.getId(), UUID.randomUUID());

        assertThat(activityRepositoryByCourseIdAndActivityId).isNull();
    }

    @Test
    @DisplayName("returns activity when given an course id and activity id")
    void findByCourseIdAndActivityId_givenCourseIdAndActivityId_returnsActivity() {
        Activity activityRepositoryByCourseIdAndActivityId = activityRepository.findByCourseIdAndActivityId(course.getId(), activity.getId());

        assertThat(activityRepositoryByCourseIdAndActivityId).isNotNull();
        assertThat(activityRepositoryByCourseIdAndActivityId.getId()).isEqualTo(activity.getId());
    }
}
