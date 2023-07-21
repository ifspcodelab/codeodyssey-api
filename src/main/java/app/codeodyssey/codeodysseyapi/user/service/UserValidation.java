package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.TokenException;
import app.codeodyssey.codeodysseyapi.common.exception.TokenProblem;
import app.codeodyssey.codeodysseyapi.common.exception.UserAlreadyValidatedException;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserValidation {
    private final UserRepository userRepository;

    public User validateUser(String token) {
        Optional<User> userOptional = userRepository.getUserByToken(token);

        if(userOptional.isPresent()) {
            User user = userOptional.get();

            if (user.isValidated()) {
                throw new UserAlreadyValidatedException("User is already validated");
            }

            if(Instant.now().isBefore(user.getCreatedAt().plus(3, ChronoUnit.MINUTES))) {
                user.setValidated(true);
                userRepository.save(user);

                return user;
            }

            throw new TokenException(TokenProblem.EXPIRED.getMessage());
        }

        throw new TokenException(TokenProblem.INEXISTENT.getMessage());
    }
}
