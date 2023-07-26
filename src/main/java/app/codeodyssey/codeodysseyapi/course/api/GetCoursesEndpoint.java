package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCoursesService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/courses")
@AllArgsConstructor
public class GetCoursesEndpoint {
    private final GetCoursesService getCoursesService;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> get() {
        return ResponseEntity.ok(getCoursesService.execute());
    }
}
