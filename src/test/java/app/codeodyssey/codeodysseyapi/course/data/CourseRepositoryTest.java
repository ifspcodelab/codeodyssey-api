package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.DatabaseContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { DatabaseContainerInitializer.class })
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
    void findAllByOrderByNameAscEndDateAsc_givenEmptyDatabase_returnEmptyList() {
        testEntityManager.flush();

        List<Course> courseList = courseRepository.findAllByOrderByNameAscEndDateAsc();
        System.out.println(courseList.toString());

        assertThat(courseList).isEmpty();
    }
}
