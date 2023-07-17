package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCoursesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
@Tag(name = "Courses", description = "Course API endpoint")
public class GetCoursesEndpoint {
    private final GetCoursesService getCoursesService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> get() {
        return ResponseEntity.ok(getCoursesService.execute());
    }
}
