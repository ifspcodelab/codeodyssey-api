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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@AllArgsConstructor
public class CreateUserService {
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final SendEmailService sendEmailService;

    @Transactional
    public UserResponse execute(@Valid CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ViolationException(Resource.USER, ViolationType.ALREADY_EXISTS, "Email already exists");
        }






        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(command.password());


        User user = new User(command.email(), command.name(), hashedPassword);

        String token = CreateRegisterTokenService.generateToken(user.getId());
        user.setToken(token);

        user = userRepository.save(user);

        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        String confirmationLink = "http://localhost:8080/api/v1/users/confirmation/" + encodedToken;
        sendEmailService.sendConfirmationEmail(user.getEmail(), confirmationLink);

        return userMapper.to(user);
    }
}