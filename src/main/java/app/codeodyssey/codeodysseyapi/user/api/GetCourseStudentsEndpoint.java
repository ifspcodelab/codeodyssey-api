package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.GetCourseStudentsService;
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
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class GetCourseStudentsEndpoint {
    private final GetCourseStudentsService getCourseStudentsService;

    @GetMapping("{professorId}/courses/{courseSlug}/students")
    public ResponseEntity<List<UserResponse>> get(
            @PathVariable @Valid UUID professorId, @PathVariable @Valid String courseSlug) {
        return ResponseEntity.ok(getCourseStudentsService.execute(professorId, courseSlug));
    }
}
