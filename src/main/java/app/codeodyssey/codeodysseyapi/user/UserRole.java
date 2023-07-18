package app.codeodyssey.codeodysseyapi.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    STUDENT("Student"),
    PROFESSOR("Professor");

    private String name;
}
