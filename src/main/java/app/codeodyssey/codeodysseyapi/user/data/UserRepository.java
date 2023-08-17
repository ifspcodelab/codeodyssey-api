package app.codeodyssey.codeodysseyapi.user.data;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);

    Optional<User> getUserByToken(String token);

    List<User> findByIsValidated(boolean isValidated);

    Optional<User> findByEmail(String email);
}
