package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailConfirmationService {
    private final UserRepository userRepository;

    public User confirmEmail(String token) {
        if (userRepository.existsByToken(token)) {
            if (ValidateRegisterTokenService.isTokenValid(token)) {
                UUID userId = GetUserIdFromRegisterTokenService.getUserId(token);
                Optional<User> userOptional = userRepository.findById(userId);

                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    user.setValidated(true);

                    userRepository.save(user);
                    return user;

                } else {
                    throw new ResourceNotFoundException(userId, Resource.USER);
                }
            }
            throw new ExpiredTokenException("Expired token", HttpStatus.BAD_REQUEST.value());
        }

        throw  new InexistentTokenException("Token does not exist", HttpStatus.BAD_REQUEST.value());
    }
}
