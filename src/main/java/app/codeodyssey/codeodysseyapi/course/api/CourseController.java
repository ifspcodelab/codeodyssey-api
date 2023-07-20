package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.CourseMapper;
import app.codeodyssey.codeodysseyapi.course.CourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<List<CourseDTO>> index() {
        return ResponseEntity.ok(courseMapper.to(courseService.findAll()));
    }

    @PostMapping
    public ResponseEntity<CourseDTO> create(@Valid @RequestBody CourseCreateDTO courseCreateDTO) {
        Course course = courseService.create(courseCreateDTO);
        CourseDTO courseDTO = courseMapper.to(course);

        return ResponseEntity.ok(courseDTO);
    }

}