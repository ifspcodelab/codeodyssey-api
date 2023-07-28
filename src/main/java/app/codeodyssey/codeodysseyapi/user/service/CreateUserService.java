package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationType;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserService {
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SendEmailService sendEmailService;

    @Transactional
    public UserResponse execute(@Valid CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ViolationException(Resource.USER, ViolationType.ALREADY_EXISTS, "Email already exists");
        }

        User user = new User(command.email(), command.name(), this.passwordEncoder.encode(command.password()));
        user = userRepository.save(user);

        sendEmailService.sendEmail(user.getEmail());

        return userMapper.to(user);
    }
}