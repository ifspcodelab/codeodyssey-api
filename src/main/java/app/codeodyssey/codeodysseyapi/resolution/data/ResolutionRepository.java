package app.codeodyssey.codeodysseyapi.resolution.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, UUID> {
//    @Query(
//            """
//            SELECT resolution
//            FROM Resolution resolution
//            WHERE resolution.student.id = :studentId
//            AND resolution.activity = :activityId
//            AND resolution.status = :resolutionStatus
//    """)
    List<Resolution> findAllByStudentIdAndActivityIdAndStatus(UUID studentId, UUID activityId, ResolutionStatus resolutionStatus);
}
