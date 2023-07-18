package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailConfirmService {
    private final UserRepository userRepository;
    public ResponseEntity<String> confirmEmail(String token) {
        if (ValidateTokenService.isTokenValid(token)) {
            return ResponseEntity.ok("Validado");

        } else {
            UUID userId = GetUserIdFromTokenService.getUserId(token);

            if (userId != null) {
                userRepository.deleteById(userId);
            }

            return ResponseEntity.badRequest().body("Token expirado");
        }
    }
}
