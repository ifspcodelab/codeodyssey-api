package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationException;
import app.codeodyssey.codeodysseyapi.common.exception.ViolationType;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserService {
    private UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse execute(CreateUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new ViolationException(Resource.USER, ViolationType.ALREADY_EXISTS, "Email already exists");
        }

        User user = new User(command.email(), command.name(),
                command.password(), command.role());

        user = userRepository.save(user);

        return userMapper.to(user);
    }
}
