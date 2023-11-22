package app.codeodyssey.codeodysseyapi.result.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "results")
@Getter
@Setter
@AllArgsConstructor
public class Result {

    @Id
    private UUID id;

    private String name;

    private Double time;

    private String error;

    @ManyToOne(fetch = FetchType.EAGER)
    private Activity activity;

    @OneToMany(mappedBy = "result", fetch = FetchType.EAGER)
    private List<TestCase> testCases;

    public Result() {
        this.id = UUID.randomUUID();
    }

    public Result(UUID id, String name, Double time, String error, Activity activity) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.error = error;
        this.activity = activity;
    }
}
