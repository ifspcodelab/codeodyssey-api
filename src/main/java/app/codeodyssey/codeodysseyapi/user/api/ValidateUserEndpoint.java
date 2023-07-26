package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.UserMapper;
import app.codeodyssey.codeodysseyapi.user.service.UserValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class ValidateUserEndpoint {
    private final UserValidationService userValidationService;
    private UserMapper userMapper;

    @PatchMapping("confirmation/{token}")
    public ResponseEntity<UserResponse> patch (@PathVariable String token) {
        return ResponseEntity.ok(userMapper.to(userValidationService.validateUser(token)));
    }
}
