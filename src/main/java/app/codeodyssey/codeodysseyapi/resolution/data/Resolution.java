package app.codeodyssey.codeodysseyapi.resolution.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import app.codeodyssey.codeodysseyapi.result.data.Result;
import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "resolutions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Resolution {
    @Id
    private UUID id;

    @ManyToOne
    private Activity activity;

    @ManyToOne
    private User student;

    private Instant submitDate;

    private String resolutionFile;

    public Resolution(Activity activity, User student, String resolutionFile) {
        this.id = UUID.randomUUID();
        this.activity = activity;
        this.student = student;
        this.submitDate = Instant.now();
        this.resolutionFile = resolutionFile;
    }
}
