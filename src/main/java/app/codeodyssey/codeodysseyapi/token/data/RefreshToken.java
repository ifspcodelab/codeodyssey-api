package app.codeodyssey.codeodysseyapi.token.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refreshtokens")
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    private UUID id;

    @ManyToOne
    private User user;

    private Instant expiryAt;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus status;

    public RefreshToken() {
        this.id = UUID.randomUUID();
        this.status = RefreshTokenStatus.UNUSED;
    }
}
