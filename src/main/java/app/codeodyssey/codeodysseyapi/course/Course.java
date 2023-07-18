package app.codeodyssey.codeodysseyapi.course;

import app.codeodyssey.codeodysseyapi.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    private UUID id;
    private String name;
    private String slug;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne
    private User professor;
    private Instant createdAt;

    public Course (String name, String slug, LocalDate startDate, LocalDate endDate, User professor){
        this.id = UUID.randomUUID();
        this.name = name;
        this.slug = slug;
        this.startDate = startDate;
        this.endDate = endDate;
        this.professor = professor;
        this.createdAt = Instant.now();
    }
}
