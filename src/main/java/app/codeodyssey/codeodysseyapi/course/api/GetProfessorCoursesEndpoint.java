package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetProfessorCoursesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Professor's Courses", description = "Professor's Courses API endpoint")
public class GetProfessorCoursesEndpoint {
    private final GetProfessorCoursesService getProfessorCoursesService;

    @GetMapping("user/{id}/courses")
    public ResponseEntity<List<CourseResponse>> get(@PathVariable @Valid UUID id) {
        return ResponseEntity.ok(getProfessorCoursesService.execute(id));
    }
}
