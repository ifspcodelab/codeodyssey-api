package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserCleanupService {
    private final UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    public void cleanupUser() {
        System.out.println("Cleanup acessado");
        List<User> nonValidatedUsers = userRepository.findByIsValidated(false);

        List<User> usersToDelete = new ArrayList<>();
        for (User user : nonValidatedUsers) {
            if (ValidateRegisterTokenService.isTokenValid(user.getToken())) {
                usersToDelete.add(user);
            }
        }
        userRepository.deleteAllInBatch(usersToDelete);
    }
}
