package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.course.util.UserFactory;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

@DataJpaTest
@DisplayName("Tests for Course Repository")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {DatabaseContainerInitializer.class})
public class CourseRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private User user;
    private Course course;

    @BeforeEach
    public void setup() {
        user = UserFactory.createValidUser();
        course = CourseFactory.createValidCourseWithProfessor(user);

        entityManager.persist(user);
        entityManager.persist(course);
        entityManager.flush();
    }

    @Test
    @DisplayName("existsBySlugAndProfessor")
    void existsBySlugAndProfessor_givenExistingSlugAndProfessor_returnTrue() {
        boolean exists = courseRepository.existsBySlugAndProfessor(course.getSlug(), user);
        Assertions.assertTrue(exists);
    }

    @Test
    @DisplayName("save a valid token when successful")
    void existsBySlugAndProfessor_givenNonExistingSlugAndProfessor_returnFalse() {
        boolean exists = courseRepository.existsBySlugAndProfessor("non-existing-slug", new User("UserName", "non-existing-email", "Password", UserRole.PROFESSOR));
        Assertions.assertFalse(exists);
    }
}
