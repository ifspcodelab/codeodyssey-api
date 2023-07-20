package app.codeodyssey.codeodysseyapi.invitation.utils;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.utils.CourseFactory;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;

import java.time.LocalDate;

public class InvitationFactory {
    private InvitationFactory() {}

    public static Invitation sampleInvitation() {
        return new Invitation(
                LocalDate.of(2023, 7, 3),
                CourseFactory.sampleCourse()
        );
    }

    public static Invitation sampleInvitationWithCourse(Course course) {
        var invitation = sampleInvitation();
        invitation.setCourse(course);

        return invitation;
    }
}
