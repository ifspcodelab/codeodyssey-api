package app.codeodyssey.codeodysseyapi.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    private UUID id;
    private String email;
    private String name;
    private String password;
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
