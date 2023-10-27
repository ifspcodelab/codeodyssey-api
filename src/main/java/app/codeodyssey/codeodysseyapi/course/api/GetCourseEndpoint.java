package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Courses", description = "Course API endpoint")
public class GetCourseEndpoint {

    private final GetCourseService getCourseService;

    @Operation(
            summary = "Get a course.",
            description = "Returns a course based on its id.",
            tags = {"Courses"})
            @ApiResponses({
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(
                                            schema = @Schema(implementation = CourseResponse.class),
                                            mediaType = "application/json")
                            }),
            @ApiResponse(
                    responseCode = "403",
                    content = {
                            @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> get(@PathVariable UUID id, Authentication authentication) {
        return new ResponseEntity<>(this.getCourseService.execute(id, authentication), HttpStatus.OK);
    }

}
