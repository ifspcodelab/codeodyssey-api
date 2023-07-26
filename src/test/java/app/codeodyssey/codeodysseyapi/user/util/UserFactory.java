package app.codeodyssey.codeodysseyapi.user.util;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;

import java.time.Instant;
import java.util.UUID;

public class UserFactory {

    public static User createValidUser(){
        return new User(UUID.randomUUID(),
                "name",
                "email@email.com",
                "$2a$10$Q8FZ0sHePMBw0fmUBG0xou/mfI3L.lzAKR3ErTIYihxRuj/wbigIm",
                UserRole.STUDENT,
                Instant.now());
    }

    public static User createValidProfessor(){
        return new User(UUID.randomUUID(),
                "name",
                "email@email.com",
                "$2a$10$Q8FZ0sHePMBw0fmUBG0xou/mfI3L.lzAKR3ErTIYihxRuj/wbigIm",
                UserRole.PROFESSOR,
                Instant.now());
    }
}
