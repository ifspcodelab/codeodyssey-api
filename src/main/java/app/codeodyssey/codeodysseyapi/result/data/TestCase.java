package app.codeodyssey.codeodysseyapi.result.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    private Result result;

    private String testName;
    private Boolean success;
    private String info;

    private Double time;

    public TestCase() {
        this.id = UUID.randomUUID();
    }

    public TestCase(UUID id, String testName, boolean success, String info, Double time, Result result) {
        this.id = id;
        this.testName = testName;
        this.success = success;
        this.info = info;
        this.time = time;
        this.result = result;
    }
}
