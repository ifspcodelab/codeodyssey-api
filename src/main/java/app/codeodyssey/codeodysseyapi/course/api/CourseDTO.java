package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CourseDTO {
    UUID id;
    String name;
    String slug;
    LocalDate startDate;
    LocalDate endDate;
    User professor;
    Instant createdAt;
}
