package app.codeodyssey.codeodysseyapi.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Value
public class CourseCreateDTO {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    String name;
    @NotNull
    @NotBlank
    @Size(min = 1, max = 255)
    String slug;
    @NotNull
    LocalDate startDate;
    LocalDate endDate;
    @NotNull
    UUID professorId;
    Instant createdAt;
}
