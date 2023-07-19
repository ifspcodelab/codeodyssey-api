package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailConfirmationService {
    private final UserRepository userRepository;

    public ResponseEntity<String> confirmEmail(String token) {
        if (ValidateTokenService.isTokenValid(token)) {
            UUID userId = GetUserIdFromTokenService.getUserId(token);
            Optional<User> userOptional = userRepository.findById(userId);

            if (userOptional.isPresent()) {
                User user = userOptional.get();
                user.setValidated(true);

                userRepository.save(user);

                return ResponseEntity.ok("Validado");

            } else {
                return ResponseEntity.badRequest().body("User not found");
            }
        }
        return ResponseEntity.badRequest().body("Expired token");
    }
}
