package app.codeodyssey.codeodysseyapi.resolution.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResolutionRepository extends JpaRepository<Resolution, UUID> {
    boolean existsByStudentIdAndActivityIdAndStatus(UUID studentId, UUID activityId, ResolutionStatus resolutionStatus);

    @Query(
            """
            SELECT resolution
            FROM Resolution resolution
            JOIN resolution.activity activity
            WHERE resolution.student.id = :studentId
            AND activity.course.id = :courseId
            AND resolution.activity.id = :activityId
            ORDER BY resolution.submitDate DESC
    """)
    List<Resolution> findAllByStudentIdAndCourseIdAndActivityIdOrderBySubmitDate(UUID studentId, UUID courseId, UUID activityId);

    boolean existsByActivityIdAndId(UUID activity, UUID resolutionId);

    boolean existsByStudentIdAndId(UUID studentId, UUID activityId);
}
