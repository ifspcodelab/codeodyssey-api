package app.codeodyssey.codeodysseyapi.activity.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    boolean existsByCourseIdAndId(UUID courseId, UUID activityId);

    @Query(
            """
            SELECT activity
            FROM Activity activity
            WHERE activity.course.id = :courseId
            AND activity.id = :activityId
    """)
    Activity findByCourseIdAndActivityId(UUID courseId, UUID activityId);
}
