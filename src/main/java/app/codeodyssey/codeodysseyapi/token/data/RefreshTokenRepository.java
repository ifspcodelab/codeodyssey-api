package app.codeodyssey.codeodysseyapi.token.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Modifying
    @Query("update RefreshToken r set r.status = 'USED' where r.id = :id")
    void setUsedById(@Param("id") UUID id);
}
