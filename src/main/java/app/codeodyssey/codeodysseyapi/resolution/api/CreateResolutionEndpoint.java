package app.codeodyssey.codeodysseyapi.resolution.api;

import app.codeodyssey.codeodysseyapi.resolution.service.CreateResolutionCommand;
import app.codeodyssey.codeodysseyapi.resolution.service.CreateResolutionService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/activities/{activityId}/resolutions")
@AllArgsConstructor
public class CreateResolutionEndpoint {
    private final CreateResolutionService createResolutionService;

    @PostMapping
    public ResponseEntity<ResolutionResponse> post(
            @PathVariable UUID courseId, @PathVariable UUID activityId, @Valid @RequestBody CreateResolutionCommand command, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createResolutionService.execute(courseId, activityId, command, username), HttpStatus.CREATED);
    }
}
