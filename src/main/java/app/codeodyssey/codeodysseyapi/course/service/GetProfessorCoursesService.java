package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetProfessorCoursesService {
    private final CourseRepository courseRepository;
}
