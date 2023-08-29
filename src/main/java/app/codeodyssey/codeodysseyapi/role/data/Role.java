package app.codeodyssey.codeodysseyapi.role.data;

import app.codeodyssey.codeodysseyapi.user.data.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Role {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
    private List<User> users;

    public Role() {
        this.id = UUID.randomUUID();
    }

    public Role(UUID id, RoleType role) {
        this.id = id;
        this.role = role;
    }
}
