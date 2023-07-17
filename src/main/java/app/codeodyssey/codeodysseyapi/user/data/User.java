package app.codeodyssey.codeodysseyapi.user.data;

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
}
