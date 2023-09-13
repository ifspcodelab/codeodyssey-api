package app.codeodyssey.codeodysseyapi.invitation.data;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "invitations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Invitation {
    @Id
    private UUID id;

    private String link;

    private LocalDate expirationDate;

    @ManyToOne
    private Course course;

    private Instant createdAt;

    public Invitation(UUID id, String link, LocalDate expirationDate, Course course) {
        this.id = id;
        this.link = link;
        this.expirationDate = expirationDate;
        this.course = course;
        this.createdAt = Instant.now();
    }
}
