package app.codeodyssey.codeodysseyapi.activity.data;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

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

    @Lob
    private Byte[] initialFile;

    @Lob
    private Byte[] solutionFile;

    @Lob
    private Byte[] testFile;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;

    public Activity() {
        this.id = UUID.randomUUID();
    }
}
