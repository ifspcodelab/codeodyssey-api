package app.codeodyssey.codeodysseyapi.user;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping
    public List<User> list(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User findById(@PathVariable UUID id) {
        return userService.findById(id);
    }

    @PostMapping
    public User create(@RequestBody User user){
        return userRepository.save(user);
    }
}