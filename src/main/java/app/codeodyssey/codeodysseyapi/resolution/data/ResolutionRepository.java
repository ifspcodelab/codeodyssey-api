package app.codeodyssey.codeodysseyapi.resolution.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, UUID> {
    List<Resolution> findAllByActivityIdAndStudentId(UUID actvityId, UUID studentId);
}
