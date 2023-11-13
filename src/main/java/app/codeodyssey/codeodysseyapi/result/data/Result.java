package app.codeodyssey.codeodysseyapi.result.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
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

    @ManyToOne(fetch = FetchType.EAGER)
    private Activity activity;

    @OneToMany(mappedBy = "result")
    private List<TestCase> testCases;

    public Result() {
        this.id = UUID.randomUUID();
    }
}
