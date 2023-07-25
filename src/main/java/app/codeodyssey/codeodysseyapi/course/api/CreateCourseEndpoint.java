package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.CreateCourseCommand;
import app.codeodyssey.codeodysseyapi.course.service.CreateCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users/{professorId}/courses")
@AllArgsConstructor
public class CreateCourseEndpoint {
    private final CreateCourseService courseService;

    @Operation(
            summary = "Create courses.",
            description =
                    "Register a new course on the database",
            tags = {"Courses"})
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {
                            @Content(
                                    array = @ArraySchema(schema = @Schema(implementation = CourseResponse.class)),
                                    mediaType = "application/json")
                    })
    })

    @PostMapping
    public ResponseEntity<CourseResponse> post(
            @PathVariable UUID professorId, @Valid @RequestBody CreateCourseCommand command) {
        return new ResponseEntity<>(courseService.execute(professorId, command), HttpStatus.CREATED);
    }
}
