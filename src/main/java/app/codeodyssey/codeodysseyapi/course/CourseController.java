package app.codeodyssey.codeodysseyapi.course;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@AllArgsConstructor
public class CourseController {
    private final CourseRepository courseRepository;

    @GetMapping
    public List<Course> list(){
        return courseRepository.findAll();
    }

    @PostMapping
    public void create(@RequestBody Course course){
        courseRepository.save(course);
    }

}
