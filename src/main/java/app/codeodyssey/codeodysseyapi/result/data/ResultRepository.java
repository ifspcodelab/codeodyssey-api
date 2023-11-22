package app.codeodyssey.codeodysseyapi.result.data;

import app.codeodyssey.codeodysseyapi.activity.data.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResultRepository extends JpaRepository<Result, UUID> {
    List<Result> findAllByActivity(Activity activity);
}
