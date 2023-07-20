package app.codeodyssey.codeodysseyapi.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetUserService {
    private final UserRepository userRepository;

    public User execute(UUID id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException());
    }
}