package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenProblem;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserValidationService {
    private final UserRepository userRepository;
    @Value("${time.register-expiration-time}")
    private int expirationTime;

    public User validateUser(String token) {
        Optional<User> userOptional = userRepository.getUserByToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isValidated()) {
                throw new UserAlreadyValidatedException("User is already validated");
            }

            Instant currentTime = Instant.now();
            Instant userCreationTime = user.getCreatedAt();
            Duration timeElapsed = Duration.between(userCreationTime, currentTime);
            long secondsElapsed = timeElapsed.getSeconds();

            if(secondsElapsed <= expirationTime) {
                user.setValidated(true);
                userRepository.save(user);

                return user;
            }

            throw new TokenException(TokenProblem.EXPIRED.getMessage());
        }

        throw new TokenException(TokenProblem.INEXISTENT.getMessage());
    }
}
