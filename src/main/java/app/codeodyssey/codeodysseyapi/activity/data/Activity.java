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

    private Instant startDate;

    private Instant endDate;

    public Activity(String title, Course course, Instant startDate, Instant endDate) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
