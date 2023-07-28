package app.codeodyssey.codeodysseyapi.enrollment.data;

import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enrollment {
    @Id
    private UUID id;

    @ManyToOne
    private Invitation invitation;

    @ManyToOne
    private User student;

    private Instant createdAt;

    public Enrollment(Invitation invitation, User student) {
        this.id = UUID.randomUUID();
        this.invitation = invitation;
        this.student = student;
        this.createdAt = Instant.now();
    }
}
