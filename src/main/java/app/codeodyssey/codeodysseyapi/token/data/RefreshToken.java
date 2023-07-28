package app.codeodyssey.codeodysseyapi.token.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "refreshtokens")
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private Instant expiryAt;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus status;

    public RefreshToken() {
        this.id = UUID.randomUUID();
        this.status = RefreshTokenStatus.UNUSED;
    }
}
