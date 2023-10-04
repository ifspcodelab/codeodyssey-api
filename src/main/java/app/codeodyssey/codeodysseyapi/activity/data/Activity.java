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

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    public Activity() {
        this.id = UUID.randomUUID();
    }
}
