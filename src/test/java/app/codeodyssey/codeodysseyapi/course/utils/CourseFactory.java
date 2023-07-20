package app.codeodyssey.codeodysseyapi.course.utils;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.utils.UserFactory;
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
        var course = sampleCourse();
        course.setProfessor(user);

        return course;
    }

    public static Course sampleCourseB() {
        return new Course(
                "Spring Security",
                "spring-security",
                LocalDate.of(2023, 7, 10),
                LocalDate.of(2023, 7, 14),
                UserFactory.sampleUserProfessor());
    }

    public static Course sampleCourseBWithProfessor(User user) {
        var course = sampleCourseB();
        course.setProfessor(user);

        return course;
    }
}
