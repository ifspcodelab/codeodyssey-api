package app.codeodyssey.codeodysseyapi.user.data;

import jakarta.persistence.*;
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
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private Instant createdAt;

    public User (String name, String email, String password, UserRole role){
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = Instant.now();
    }
}
