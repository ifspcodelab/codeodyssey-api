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

    private String description;

    @ManyToOne
    private Course course;

    private Instant startDate;

    private Instant endDate;

    private String initialFile;

    private String solutionFile;

    private String testFile;

    private String extension;

    public Activity(String title, String description, Course course, Instant startDate, Instant endDate,
                    String initialFile, String solutionFile, String testFile, String extension) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.course = course;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialFile = initialFile;
        this.solutionFile = solutionFile;
        this.testFile = testFile;
        this.extension = extension;
    }
}
