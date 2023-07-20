package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceName;
import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetCourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseResponse execute(UUID id) {
        return courseMapper.to(courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.COURSE, id)));
    }
}
