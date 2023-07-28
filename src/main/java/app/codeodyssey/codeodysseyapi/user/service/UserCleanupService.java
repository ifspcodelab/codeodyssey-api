package app.codeodyssey.codeodysseyapi.user.service;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserCleanupService {
    private final UserRepository userRepository;
    @Value("${time.register-expiration-time}")
    private int expirationTime;

    @Scheduled(fixedRateString = "${scheduler.registration.interval}")
    public void cleanupUser() {
        System.out.println("Cleanup acessado");
        List<User> nonValidatedUsers = userRepository.findByIsValidated(false);
        List<User> usersToDelete = new ArrayList<>();

        for (User user : nonValidatedUsers) {
            if(Instant.now().isAfter(user.getCreatedAt().plus(expirationTime, ChronoUnit.SECONDS))) {
                usersToDelete.add(user);
            }
        }
        userRepository.deleteAllInBatch(usersToDelete);
    }
}
