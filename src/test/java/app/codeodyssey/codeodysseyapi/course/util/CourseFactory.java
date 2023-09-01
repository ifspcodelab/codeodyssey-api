package app.codeodyssey.codeodysseyapi.course.util;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;

public final class CourseFactory {
    private CourseFactory() {}

    public static Course createValidCourse() {
        return new Course("CourseName", "Slug", LocalDate.now(), LocalDate.now(), UserFactory.createValidProfessor());
    }

    public static Course createValidCourseWithProfessor(User user) {
        var course = createValidCourse();
        course.setProfessor(user);

        return course;
    }

    public static Course sampleCourse() {
        return new Course(
                "Spring MVC",
                "spring-mvc",
                LocalDate.now(),
                LocalDate.now(),
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
                LocalDate.now(),
                LocalDate.now(),
                UserFactory.sampleUserProfessor());
    }

    public static Course sampleCourseBWithProfessor(User user) {
        var course = sampleCourseB();
        course.setProfessor(user);

        return course;
    }

    public static Course sampleCourseC() {
        return new Course(
                "Spring Cloud",
                "spring-cloud",
                LocalDate.now(),
                LocalDate.now(),
                UserFactory.sampleUserProfessor());
    }

    public static Course sampleCourseCWithProfessor(User user) {
        var course = sampleCourseC();
        course.setProfessor(user);

        return course;
    }
}
