package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.CreateCourseCommand;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
public class CreateCourseEndpoint {
    private final CreateCourseService createCourseService;

    @PostMapping
    public ResponseEntity<CourseResponse> post(@Valid @RequestBody CreateCourseCommand command) {
        return new ResponseEntity<>(createCourseService.execute(command), HttpStatus.CREATED);
    }
}
