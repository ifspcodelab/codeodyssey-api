package app.codeodyssey.codeodysseyapi.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findById(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }
}