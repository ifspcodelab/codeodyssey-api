package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.UserMapper;
import app.codeodyssey.codeodysseyapi.user.service.UserValidation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class ValidateUserEndpoint {
    private final UserValidation userValidation;
    private UserMapper userMapper;

    @GetMapping("confirmation/{token}")
    public ResponseEntity<UserResponse> get (@PathVariable String token) {
        return ResponseEntity.ok(userMapper.to(userValidation.validateUser(token)));
    }
}
