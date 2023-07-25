package app.codeodyssey.codeodysseyapi.token.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "refreshtokens")
//@SQLDelete(sql = "UPDATE refreshtokens SET status = 'USED' where id = ?")
@AllArgsConstructor
@Getter
@Setter
public class RefreshToken {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String token;
    private Instant expiryAt;

    @Enumerated(EnumType.STRING)
    private RefreshTokenStatus status;

    public RefreshToken() {
        this.id = UUID.randomUUID();
        this.status = RefreshTokenStatus.UNUSED;
    }
}
