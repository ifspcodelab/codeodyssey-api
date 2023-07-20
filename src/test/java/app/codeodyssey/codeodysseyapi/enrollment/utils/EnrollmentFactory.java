package app.codeodyssey.codeodysseyapi.enrollment.utils;

import app.codeodyssey.codeodysseyapi.enrollment.data.Enrollment;
import app.codeodyssey.codeodysseyapi.invitation.utils.InvitationFactory;
import app.codeodyssey.codeodysseyapi.user.utils.UserFactory;

public class EnrollmentFactory {
    private EnrollmentFactory() {}

    public static Enrollment sampleEnrollment() {
        return new Enrollment(
                InvitationFactory.sampleInvitation(),
                UserFactory.sampleUserStudent()
        );
    }
}
