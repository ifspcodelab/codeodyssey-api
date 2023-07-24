package app.codeodyssey.codeodysseyapi.enrollment.util;

import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.user.data.User;

public class EnrollmentFactory {
    private EnrollmentFactory() {}

    public static Enrollment sampleEnrollment(Invitation invitation, User student) {
        return new Enrollment(invitation, student);
    }
}
