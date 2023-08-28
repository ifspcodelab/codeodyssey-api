package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.ExpiredTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.NoneExistentTokenException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenProblem;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserRepository userRepository;

    @Value("${time.register-expiration-time}")
    private int expirationTime;

    public User validateUser(String token) {
        Optional<User> userOptional = userRepository.getUserByToken(token);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isValidated()) {
                throw new UserAlreadyValidatedException("User is already validated");
            }

            LocalDateTime userCreationTime = user.getCreatedAt();
            Duration timeElapsed = Duration.between(userCreationTime, LocalDateTime.now());
            long secondsElapsed = timeElapsed.getSeconds();

            if (secondsElapsed <= expirationTime) {
                user.setValidated(true);
                userRepository.save(user);

                return user;
            }

            throw new ExpiredTokenException(TokenProblem.EXPIRED.getMessage());
        }

        throw new NoneExistentTokenException(TokenProblem.NONEXISTENT.getMessage());
    }
}
