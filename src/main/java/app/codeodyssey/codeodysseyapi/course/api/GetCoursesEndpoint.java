package app.codeodyssey.codeodysseyapi.course.api;

import app.codeodyssey.codeodysseyapi.course.service.GetCoursesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/courses")
@AllArgsConstructor
@Tag(name = "Courses", description = "Course API endpoint")
public class GetCoursesEndpoint {
    private final GetCoursesService getCoursesService;

    @Operation(
            summary = "Get all courses.",
            description =
                    "Returns a list containing all courses registered on the database. This course list is ordered by name and end date.",
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
    @GetMapping
    public ResponseEntity<List<CourseResponse>> get(Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return ResponseEntity.ok(getCoursesService.execute(username));
    }
}
