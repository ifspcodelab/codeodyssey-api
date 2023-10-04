package app.codeodyssey.codeodysseyapi.activity.data;

import app.codeodyssey.codeodysseyapi.course.data.Course;
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
@Table(name = "activities")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Activity {
    @Id
    private UUID id;

    private String title;

    @ManyToOne
    private Course course;

    private LocalDate startDate;

    private LocalDate endDate;

    private Byte initialFile;

    private Instant createdAt;

    public Activity(String title, Course course, LocalDate startDate, LocalDate endDate, Byte initialFile) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialFile = initialFile;
        this.createdAt = Instant.now();
    }
}
