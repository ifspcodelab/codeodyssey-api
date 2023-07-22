package app.codeodyssey.codeodysseyapi.user.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByEmail(String email);
    User getUserByEmail(String email);
    Optional<User> getUserByToken(String token);
    List<User> findByIsValidated(boolean isValidated);

}
