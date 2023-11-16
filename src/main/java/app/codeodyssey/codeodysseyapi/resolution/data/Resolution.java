package app.codeodyssey.codeodysseyapi.resolution.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
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

    @Enumerated(EnumType.STRING)
    private ResolutionStatus status;

    public Resolution(Activity activity, User student, String resolutionFile) {
        this.id = UUID.randomUUID();
        this.activity = activity;
        this.student = student;
        this.submitDate = Instant.now();
        this.resolutionFile = resolutionFile;
        this.status = ResolutionStatus.WAITING_FOR_RESULTS;
    }
}
