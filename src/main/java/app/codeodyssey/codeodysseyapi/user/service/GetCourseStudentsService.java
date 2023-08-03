package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.common.exception.*;
import app.codeodyssey.codeodysseyapi.user.api.UserResponse;
import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GetCourseStudentsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserResponse> execute(UUID professorId, UUID courseId) {
        User professor = userRepository
                .findById(professorId)
                .orElseThrow(() -> new ResourceNotFoundException(professorId, Resource.USER));

        if (!professor.getRole().equals(UserRole.PROFESSOR)) {
            throw new ForbiddenAccessException(professorId);
        }

        return userMapper.to(userRepository.findUsersByCourseIdOrderByName(courseId));
    }
}
