package app.codeodyssey.codeodysseyapi.result.data;

import app.codeodyssey.codeodysseyapi.resolution.data.Resolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, UUID> {
    Optional<Result> findByResolution(Resolution resolution);
}
