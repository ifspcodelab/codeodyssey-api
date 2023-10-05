package app.codeodyssey.codeodysseyapi.activity.api;

import app.codeodyssey.codeodysseyapi.activity.service.CreateActivityCommand;
import app.codeodyssey.codeodysseyapi.activity.service.CreateActivityService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/courses/{courseId}/activities")
@AllArgsConstructor
public class CreateActivityEndpoint {
    private final CreateActivityService createActivityService;
    @PostMapping
    public ResponseEntity<ActivityResponse> post(
            @PathVariable UUID courseId, @Valid @RequestBody CreateActivityCommand command, Authentication authentication) {
        var userDetails = (UserDetails) authentication.getPrincipal();
        var username = userDetails.getUsername();
        return new ResponseEntity<>(createActivityService.execute(courseId, command, username), HttpStatus.CREATED);
    }
}
