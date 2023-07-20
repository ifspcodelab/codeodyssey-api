package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.ExpiredTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.InexistentTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
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
                return user;
            }

            if (userRepository.getUserById(GetUserIdFromRegisterTokenService.getUserId(token)).isValidated()) {
                throw new UserAlreadyValidatedException("User is already validated", HttpStatus.BAD_REQUEST.value());
            }

            throw new ExpiredTokenException("Expired token", HttpStatus.BAD_REQUEST.value());
        }

        throw  new InexistentTokenException("Token does not exist", HttpStatus.BAD_REQUEST.value());
    }
}
