package app.codeodyssey.codeodysseyapi.activity.data;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Table(name = "activities")
@Data
public class Activity {

    @Id
    private UUID id;

    private String title;
    private String description;
    private Instant startDate;
    private Instant endDate;

    @Column(columnDefinition="BLOB")
    private byte[] initialFile;

    @Column(columnDefinition="BLOB")
    private byte[] solutionFile;

    @Column(columnDefinition="BLOB")
    private byte[] testFile;

    private String extension;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    public Activity() {
        this.id = UUID.randomUUID();
    }

    public Activity(String title,
                    String description,
                    Instant startDate,
                    Instant endDate,
                    byte[] initialFile,
                    byte[] solutionFile,
                    byte[] testFile,
                    String extension,
                    Course course) {

        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.initialFile = initialFile;
        this.solutionFile = solutionFile;
        this.testFile = testFile;
        this.extension = extension;
        this.course = course;

    }
}
