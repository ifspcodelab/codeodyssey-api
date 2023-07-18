package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetProfessorCoursesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Professor's Courses", description = "Professor's Courses API endpoint")
public class GetProfessorCoursesEndpoint {
    private final GetProfessorCoursesService getProfessorCoursesService;
}
