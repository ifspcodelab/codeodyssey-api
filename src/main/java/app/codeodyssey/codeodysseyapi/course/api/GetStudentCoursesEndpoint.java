package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetStudentCoursesService;
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
public class GetStudentCoursesEndpoint {
    private final GetStudentCoursesService getStudentCoursesService;

    @GetMapping("users/{id}/enrollments")
    public ResponseEntity<List<CourseResponse>> get(@PathVariable @Valid UUID id) {
        return ResponseEntity.ok(getStudentCoursesService.execute(id));
    }
}
