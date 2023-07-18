package app.codeodyssey.codeodysseyapi.enrollment;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invitations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Enrollment {
    @Id
    private UUID id;

    @ManyToOne
    private Invitation invitation;

    @ManyToOne
    private Course course;

    private Instant createdAt;

    public Enrollment(Invitation invitation, Course course) {
        this.id = UUID.randomUUID();
        this.invitation = invitation;
        this.course = course;
        this.createdAt = Instant.now();
    }
}
