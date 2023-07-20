package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.data.Course;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseCommand;
import app.codeodyssey.codeodysseyapi.course.service.CourseMapper;
import app.codeodyssey.codeodysseyapi.course.service.GetCoursesService;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/courses")
@AllArgsConstructor
public class CourseController {
    private final GetCoursesService getCoursesService;
    private final CreateCourseService createCourseService;
    private final CourseMapper courseMapper;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> index() {
        return ResponseEntity.ok(courseMapper.to(getCoursesService.execute()));
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CreateCourseCommand command) {
        Course course = createCourseService.execute(command);
        CourseResponse courseResponse = courseMapper.to(course);

        return ResponseEntity.ok(courseResponse);
    }

}