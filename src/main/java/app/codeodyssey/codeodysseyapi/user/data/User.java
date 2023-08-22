package app.codeodyssey.codeodysseyapi.user.data;

import app.codeodyssey.codeodysseyapi.role.data.Role;
import app.codeodyssey.codeodysseyapi.role.data.RoleType;
import app.codeodyssey.codeodysseyapi.token.data.RefreshToken;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User implements UserDetails {
    @Id
    private UUID id;

    private String email;
    private String name;
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    private Instant createdAt;
    private boolean isValidated;
    private String token;

    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens;

    public User(String email, String name, String password) {
        this.id = UUID.randomUUID();
        this.email = email;
        this.name = name;
        this.password = password;
        this.roles.add(new Role(UUID.randomUUID(), RoleType.STUDENT));
        this.createdAt = Instant.now();
        this.isValidated = false;
        this.token = UUID.randomUUID().toString();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole().name())).toList();
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
