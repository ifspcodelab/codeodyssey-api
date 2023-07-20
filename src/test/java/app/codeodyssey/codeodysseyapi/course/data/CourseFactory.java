package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserFactory;
import java.time.LocalDate;

public final class CourseFactory {
    private CourseFactory() {}

    public static Course sampleCourse() {
        return new Course(
                "Spring MVC",
                "spring-mvc",
                LocalDate.of(2023, 7, 3),
                LocalDate.of(2023, 7, 7),
                UserFactory.sampleUserProfessor());
    }

    public static Course sampleCourseWithProfessor(User user) {
        return new Course(
                "Spring MVC",
                "spring-mvc",
                LocalDate.of(2023, 7, 3),
                LocalDate.of(2023, 7, 7),
                user);
    }
}
