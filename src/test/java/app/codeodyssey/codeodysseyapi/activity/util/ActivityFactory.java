package app.codeodyssey.codeodysseyapi.activity.util;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.course.data.Course;

import java.time.Instant;

public final class ActivityFactory {

    private ActivityFactory() {}

    public static Activity sampleActivity(Course course) {
        return new Activity(
                "title",
                "description",
                Instant.now(),
                Instant.now().plusSeconds(600),
                "test",
                "test",
                "test",
                ".java",
                course
        );
    }
}
