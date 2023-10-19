package app.codeodyssey.codeodysseyapi.resolution.util;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.activity.util.ActivityFactory;
import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import app.codeodyssey.codeodysseyapi.user.util.UserFactory;

public final class ResolutionFactory {
    public static Resolution createValidResolution() {
        return new Resolution(
                ActivityFactory.createValidActivity(),
                UserFactory.createValidUser(),
                "ResolutionFile");
    }

    public static Resolution createValidResolutionWithActivity(Activity activity) {
        var resolution = createValidResolution();
        resolution.setActivity(activity);

        return resolution;
    }

    public static Resolution createValidResolutionB() {
        return new Resolution(
                ActivityFactory.createValidActivity(),
                UserFactory.createValidUser(),
                "ResolutionFile2");
    }

    public static Resolution createValidResolutionBWithActivity(Activity activity) {
        var resolution = createValidResolution();
        resolution.setActivity(activity);

        return resolution;
    }
}
