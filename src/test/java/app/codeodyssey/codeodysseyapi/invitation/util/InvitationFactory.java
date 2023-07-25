package app.codeodyssey.codeodysseyapi.invitation.util;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.util.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import java.time.LocalDate;

public final class InvitationFactory {
    private InvitationFactory() {}

    public static Invitation sampleInvitation() {
        return new Invitation(LocalDate.of(2023, 7, 3), CourseFactory.sampleCourse());
    }

    public static Invitation sampleInvitationWithCourse(Course course) {
        var invitation = sampleInvitation();
        invitation.setCourse(course);

        return invitation;
    }
}
