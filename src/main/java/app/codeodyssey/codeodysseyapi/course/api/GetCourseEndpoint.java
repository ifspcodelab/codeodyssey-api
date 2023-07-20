package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCourseService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
public class GetCourseEndpoint {
    private final GetCourseService getCourseService;

    @GetMapping("{id}")
    public ResponseEntity<CourseResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(getCourseService.execute(id));
    }

}