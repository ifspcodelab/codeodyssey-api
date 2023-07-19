package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.EmailConfirmationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class EmailConfirmationEndpoint {
    private final EmailConfirmationService emailConfirmationService;

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<String> post(@PathVariable String token) {
        return emailConfirmationService.confirmEmail(token);
    }
}
