package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.Resource;
import app.codeodyssey.codeodysseyapi.common.exception.ResourceNotFoundException;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserResponse execute(UUID userId) {
        return userMapper.to(
                userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(userId, Resource.USER))
        );
    }
}
