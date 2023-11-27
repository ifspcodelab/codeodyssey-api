package app.codeodyssey.codeodysseyapi.result.data;

import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
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

    @OneToOne
    private Resolution resolution;

    @OneToMany(mappedBy = "result", fetch = FetchType.EAGER)
    private List<TestCase> testCases;

    public Result() {
        this.id = UUID.randomUUID();
    }

    public Result(UUID id, String name, Double time, String error, Resolution resolution) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.error = error;
        this.resolution = resolution;
    }
}
