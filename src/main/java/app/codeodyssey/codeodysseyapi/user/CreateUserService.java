package app.codeodyssey.codeodysseyapi.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse execute(CreateUserCommand command) {
        User user = userRepository.save(new User(
                command.name(),
                command.email(),
                command.password()
        ));

        return userMapper.to(user);
    }
}
