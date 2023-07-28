package app.codeodyssey.codeodysseyapi.course.util;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;
import java.time.LocalDate;

public final class CourseFactory {
    public static Course createValidCourse() {
        return new Course("CourseName", "Slug", LocalDate.now(), LocalDate.now(), UserFactory.createValidProfessor());
    }

    public static Course createValidCourseWithProfessor(User user) {
        var course = createValidCourse();
        course.setProfessor(user);

        return course;
    }
}
