package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetUsersService {
    private final UserRepository repository;
    private final UserMapper userMapper;

    public List<UserResponse> execute() {
        return userMapper.to(repository.findAll());
    }

}