package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.api.CourseDTO;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetCoursesService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseDTO> execute() {
        return courseMapper.to(courseRepository.findAll());
    }
}
