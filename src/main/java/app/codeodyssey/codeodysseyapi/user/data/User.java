package app.codeodyssey.codeodysseyapi.user.data;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    @Id
    private UUID id;

    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Instant createdAt;

    public User(String email, String name, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = UserRole.STUDENT;
        this.createdAt = Instant.now();
    }
}
