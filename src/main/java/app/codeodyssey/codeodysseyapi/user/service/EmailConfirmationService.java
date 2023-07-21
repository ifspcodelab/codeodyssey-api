package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailConfirmationService {
    private final UserRepository userRepository;

    public User confirmEmail(String token) {
        if (userRepository.existsByToken(token)) {
            if (ValidateRegisterTokenService.isTokenValid(token)) {
                UUID userId = GetUserIdFromRegisterTokenService.getUserId(token);
                User user = userRepository.getUserById(userId);

                user.setValidated(true);
                userRepository.save(user);
                return user;
            }

            if (userRepository.getUserById(GetUserIdFromRegisterTokenService.getUserId(token)).isValidated()) {
                throw new UserAlreadyValidatedException("User is already validated");
            }

            throw new TokenException("Expired token");
        }

        throw new TokenException("Token does not exist");
    }
}
