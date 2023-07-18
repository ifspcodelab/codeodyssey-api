package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationType;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserService {
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final SendEmailService sendEmailService;

    @Transactional
    public UserResponse execute(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ViolationException(Resource.USER, ViolationType.ALREADY_EXISTS, "Email already exists");
        }


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(command.password());


        User user = new User(command.email(), command.name(),
                hashedPassword, command.role());

        user = userRepository.save(user);

        String token = CreateTokenService.generateToken(user.getId());
        String confirmationLink = token;

        sendEmailService.sendConfirmationEmail(user.getEmail(), confirmationLink);

        return userMapper.to(user);
    }
}