package app.codeodyssey.codeodysseyapi.user.data;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    private UUID id;

    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Instant createdAt;
    private boolean isValidated;
    private String token;

    public User(String email, String name, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = UserRole.STUDENT;
        this.createdAt = Instant.now();
        this.isValidated = false;
        this.token = UUID.randomUUID().toString();
    }

    public User(String name, String email) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
        this.role = UserRole.STUDENT;
        this.createdAt = Instant.now();
    }

    public User() {
        this.id = UUID.randomUUID();
    }

    public User(UUID id, String email, String name, String password, UserRole role, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.isValidated = true;
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
