package app.codeodyssey.codeodysseyapi.user;

import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceName;
import app.codeodyssey.codeodysseyapi.common.exceptions.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse execute(UUID id) {
        return userMapper.to(userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.USER, id)));
    }
}