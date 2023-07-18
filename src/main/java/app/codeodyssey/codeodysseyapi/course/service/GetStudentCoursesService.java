package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.enrollment.data.EnrollmentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetStudentCoursesService {
    private final EnrollmentRepository enrollmentRepository;
}
