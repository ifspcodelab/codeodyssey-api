package app.codeodyssey.codeodysseyapi.invitation.data;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    private Instant createdAt;

    public Invitation(LocalDate expirationDate, Course course) {
        this.id = UUID.randomUUID();
        this.link = "/invitations/%s".formatted(id);
        this.expirationDate = expirationDate;
        this.course = course;
        this.createdAt = Instant.now();
    }
}
