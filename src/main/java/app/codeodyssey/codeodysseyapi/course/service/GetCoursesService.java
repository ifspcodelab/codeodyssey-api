package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetCoursesService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public List<CourseResponse> execute() {
        return courseMapper.to(courseRepository.findAll(Sort.by(Sort.Order.asc("name"), Sort.Order.asc("end_date"))));
    }
}
