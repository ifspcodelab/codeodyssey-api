package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.GetCourseStudentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class GetCourseStudentsEndpoint {
    private final GetCourseStudentsService getCourseStudentsService;

    @Operation(
            summary = "Get all course's students.",
            description = "Returns a list containing all students of a course given an professor id and course slug."
                    + " This user list is ordered by student name.",
            tags = {"Users"})
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                content = {
                    @Content(
                            array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)),
                            mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "400",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "403",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "404",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                }),
        @ApiResponse(
                responseCode = "409",
                content = {
                    @Content(schema = @Schema(implementation = ProblemDetail.class), mediaType = "application/json")
                })
    })
    @GetMapping("{professorId}/courses/{courseSlug}/students")
    public ResponseEntity<List<UserResponse>> get(
            @PathVariable @Valid UUID professorId, @PathVariable @Valid String courseSlug) {
        return ResponseEntity.ok(getCourseStudentsService.execute(professorId, courseSlug));
    }
}
