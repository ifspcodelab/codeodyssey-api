package app.codeodyssey.codeodysseyapi.enrollment.data;

import app.codeodyssey.codeodysseyapi.invitation.data.Invitation;
import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "enrollments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Enrollment {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Invitation invitation;

    @ManyToOne(fetch = FetchType.EAGER)
    private User student;

    private Instant createdAt;

    public Enrollment(Invitation invitation, User student) {
        this.id = UUID.randomUUID();
        this.invitation = invitation;
        this.student = student;
        this.createdAt = Instant.now();
    }
}
