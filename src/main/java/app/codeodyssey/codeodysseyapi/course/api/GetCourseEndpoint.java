package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class GetCourseEndpoint {

    private final GetCourseService getCourseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> get(@PathVariable UUID id, Authentication authentication) {
        return new ResponseEntity<>(this.getCourseService.execute(id, authentication), HttpStatus.OK);
    }

}
