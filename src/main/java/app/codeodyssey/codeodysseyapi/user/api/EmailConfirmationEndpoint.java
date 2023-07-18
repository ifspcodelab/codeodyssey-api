package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.user.service.EmailConfirmService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class EmailConfirmationEndpoint {
    private final EmailConfirmService emailConfirmService;

    @PostMapping("/confirmation/{token}")
    public ResponseEntity<String> post(@PathVariable String token) {
        return emailConfirmService.confirmEmail(token);
    }
}
