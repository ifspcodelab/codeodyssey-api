package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.CreateUserCommand;
import app.codeodyssey.codeodysseyapi.user.service.CreateUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class CreateUserEndpoint {
    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<UserResponse> post(@RequestBody @Valid CreateUserCommand command) {
        return new ResponseEntity<>(createUserService.execute(command), HttpStatus.CREATED);
    }
}
