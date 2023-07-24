package app.codeodyssey.codeodysseyapi.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CreateUserEndpoint {
    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<UserResponse> post(@Valid @RequestBody CreateUserCommand command) {
        return new ResponseEntity<>(createUserService.execute(command), HttpStatus.CREATED);
    }
}
