package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenExpiredException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenMalformedException;
import app.codeodyssey.codeodysseyapi.common.exception.UserNotFoundException;
import app.codeodyssey.codeodysseyapi.user.service.EmailConfirmationService;
import app.codeodyssey.codeodysseyapi.user.service.UserMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class EmailConfirmationEndpoint {
    private final EmailConfirmationService emailConfirmationService;
    private final UserMapper userMapper;

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<UserResponse> confirmEmail(@PathVariable String token) {
        return ResponseEntity.ok(userMapper.to(emailConfirmationService.confirmEmail(token)));
    }
}
