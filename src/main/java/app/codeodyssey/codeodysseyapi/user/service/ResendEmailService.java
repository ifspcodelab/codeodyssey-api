package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.EmailNotFoundException;
import app.codeodyssey.codeodysseyapi.common.exception.ResendEmailException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationType;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class ResendEmailService {
    private UserRepository userRepository;
    private final UserMapper userMapper;
    private final SendEmailService sendEmailService;

    public UserResponse execute(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new EmailNotFoundException(email)
        );

        if (!user.getCreatedAt().plusSeconds(60).isBefore(Instant.now())) {
            throw new ResendEmailException(email);
        }

        sendEmailService.sendEmail(user.getEmail(), user.getToken());
        log.info("Confirmation email was resent to {}", user.getEmail());
        return userMapper.to(user);
    }
}
