package app.codeodyssey.codeodysseyapi.activity.util;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;

import java.time.Instant;

public final class ActivityFactory {
    public static Activity createValidActivity() {
        return new Activity(
                "ActivityTitle",
                "ActivityDescription",
                CourseFactory.createValidCourse(),
                Instant.now(),
                Instant.now().plusSeconds(86400),
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "JAVA" );
    }

    public static Activity createValidActivityWithCourse(Course course) {
        var activity = createValidActivity();
        activity.setCourse(course);

        return activity;
    }

    public static Activity createValidActivityB() {
        return new Activity(
                "ActivityTitle2",
                "ActivityDescription2",
                CourseFactory.createValidCourse(),
                Instant.now(),
                Instant.now().plusSeconds(86400),
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==",
                "JAVA" );
    }

    public static Activity createValidActivityBWithCourse(Course course) {
        var activity = createValidActivityB();
        activity.setCourse(course);

        return activity;
    }
}
