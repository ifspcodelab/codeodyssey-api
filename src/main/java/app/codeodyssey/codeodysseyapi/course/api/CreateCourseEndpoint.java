package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.CreateCourseCommand;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users/{professorId}/courses")
@AllArgsConstructor
public class CreateCourseEndpoint {
    private final CreateCourseService createCourseService;

    @PostMapping
    public ResponseEntity<CourseResponse> post(@PathVariable UUID professorId, @Valid @RequestBody CreateCourseCommand command) {
        return new ResponseEntity<>(createCourseService.execute(professorId, command), HttpStatus.CREATED);
    }
}
