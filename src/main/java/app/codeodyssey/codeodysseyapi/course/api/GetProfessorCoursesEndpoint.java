package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetProfessorCoursesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
@Tag(name = "Courses", description = "Course API endpoint")
public class GetProfessorCoursesEndpoint {
    private final GetProfessorCoursesService getProfessorCoursesService;

    @Operation(
            summary = "Get all professor's courses.",
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
    @GetMapping("user/{id}/courses")
    public ResponseEntity<List<CourseResponse>> get(@PathVariable @Valid UUID id) {
        return ResponseEntity.ok(getProfessorCoursesService.execute(id));
    }
}
