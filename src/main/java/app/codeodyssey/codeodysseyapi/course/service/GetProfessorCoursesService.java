package app.codeodyssey.codeodysseyapi.course.service;

import app.codeodyssey.codeodysseyapi.course.api.CourseResponse;
import app.codeodyssey.codeodysseyapi.course.data.CourseRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GetProfessorCoursesService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Operation(
            summary = "Get all professor's courses",
            description =
                    "Returns a list containing all courses of a professor given their id. This course list ir ordered by name and end date.",
            tags = {"Courses"})
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)),
                            mediaType = "application/json")
                })
    })
    public List<CourseResponse> execute(UUID id) {
        return courseMapper.to(courseRepository.findAllByProfessorIdOrderByNameAscEndDateAsc(id));
    }
}
