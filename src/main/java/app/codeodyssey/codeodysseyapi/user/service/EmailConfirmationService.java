package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.TokenExpiredException;
import app.codeodyssey.codeodysseyapi.common.exception.UserNotFoundException;
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
        if (!ValidateRegisterTokenService.isTokenValid(token)) {
            throw new TokenExpiredException("Expired token");
        }

        UUID userId = GetUserIdFromRegisterTokenService.getUserId(token);
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }

        User user = userOptional.get();
        user.setValidated(true);
        userRepository.save(user);

        return ResponseEntity.ok("Validated");
    }
}
