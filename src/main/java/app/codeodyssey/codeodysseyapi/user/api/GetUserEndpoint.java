package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.GetUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class GetUserEndpoint {
    private final GetUserService getUserService;

    @GetMapping("{userId}")
    public ResponseEntity<UserResponse> get(@PathVariable UUID userId) {
        return ResponseEntity.ok(getUserService.execute(userId));
    }

}
