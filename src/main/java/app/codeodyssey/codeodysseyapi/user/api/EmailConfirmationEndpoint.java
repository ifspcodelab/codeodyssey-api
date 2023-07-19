package app.codeodyssey.codeodysseyapi.user.api;

import app.codeodyssey.codeodysseyapi.common.exception.InvalidTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenExpiredException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenMalformedException;
import app.codeodyssey.codeodysseyapi.common.exception.UserNotFoundException;
import app.codeodyssey.codeodysseyapi.user.service.EmailConfirmationService;
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

    @GetMapping("/confirmation/{token}")
    public ResponseEntity<String> post(@PathVariable String token) {
        try {
            ResponseEntity<String> response = emailConfirmationService.confirmEmail(token);
            return response;

        } catch (InvalidTokenException | TokenExpiredException | TokenMalformedException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());

        } catch (UserNotFoundException ex) {
            return ResponseEntity.notFound().build();

        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }
}
