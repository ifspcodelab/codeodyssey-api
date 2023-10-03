package app.codeodyssey.codeodysseyapi.course.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER)
    private List<Activity> activities;

    public Course(String name, String slug, LocalDate startDate, LocalDate endDate, User professor) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.slug = slug;
        this.startDate = startDate;
        this.endDate = endDate;
        this.professor = professor;
        this.createdAt = Instant.now();
    }
}
