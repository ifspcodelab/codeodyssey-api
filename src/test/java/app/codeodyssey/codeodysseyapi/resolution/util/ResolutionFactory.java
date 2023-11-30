package app.codeodyssey.codeodysseyapi.resolution.util;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.resolution.data.ResolutionStatus;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;

public final class ResolutionFactory {
    public static Resolution createValidResolution() {
        return new Resolution(
                ActivityFactory.createValidActivity(),
                UserFactory.createValidUser(),
                "DQpwdWJsaWMgY2xhc3MgVmVpY3VsbyB7DQoJcHJpdmF0ZSBmbG9hdCBUb3RhbENvbWJ1c3RpdmVsOw0KCQ0KCXB1YmxpYyBWZWljdWxvKCkge30NCn0NCg==");
    }

    public static Resolution createValidResolutionWithActivity(Activity activity) {
        var resolution = createValidResolution();
        resolution.setActivity(activity);

        return resolution;
    }

    public static Resolution createValidResolutionWithActivityAndStudent(Activity activity, User student) {
        var resolution = createValidResolution();
        resolution.setActivity(activity);
        resolution.setStudent(student);

        return resolution;
    }

    public static Resolution createValidResolutionWithActivityAndStudentAndExecutedSuccessStatus(Activity activity, User student) {
        var resolution = createValidResolution();
        resolution.setActivity(activity);
        resolution.setStudent(student);
        resolution.setStatus(ResolutionStatus.EXECUTED_SUCCESS);

        return resolution;
    }
}
