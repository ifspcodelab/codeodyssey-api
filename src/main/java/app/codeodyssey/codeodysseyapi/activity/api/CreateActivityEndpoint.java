package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.activity.service.CreateActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CreateActivityEndpoint {

    private final CreateActivityService createActivityService;

    @PostMapping("/courses/{courseId}/activities")
    public ResponseEntity<ActivityResponse> post(@PathVariable UUID courseId,
                                                 @RequestBody @Valid ActivityRequest activity,
                                                 Authentication authentication) {

        return new ResponseEntity<>(
                this.createActivityService.execute(courseId, activity, authentication),
                HttpStatus.CREATED);

    }
}
