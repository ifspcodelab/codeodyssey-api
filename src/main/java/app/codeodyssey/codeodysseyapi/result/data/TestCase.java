package app.codeodyssey.codeodysseyapi.result.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "testcases")
@Getter
@Setter
@AllArgsConstructor
public class TestCase {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Result result;

    private String testName;
    private Boolean success;
    private String info;

    private Double time;

    public TestCase() {
        this.id = UUID.randomUUID();
    }
}
