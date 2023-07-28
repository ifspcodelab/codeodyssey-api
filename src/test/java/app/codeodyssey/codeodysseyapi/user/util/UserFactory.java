package app.codeodyssey.codeodysseyapi.user.util;

import app.codeodyssey.codeodysseyapi.user.data.User;
import app.codeodyssey.codeodysseyapi.user.data.UserRole;
import java.time.Instant;
import java.util.UUID;

public final class UserFactory {
    private UserFactory() {}

    public static User createValidUser() {
        return new User(
                UUID.randomUUID(),
                "name",
                "email@email.com",
                "$2a$10$Q8FZ0sHePMBw0fmUBG0xou/mfI3L.lzAKR3ErTIYihxRuj/wbigIm",
                UserRole.STUDENT,
                Instant.now());
    }

    public static User createValidProfessor() {
        return new User(
                UUID.randomUUID(),
                "name",
                "email@email.com",
                "$2a$10$Q8FZ0sHePMBw0fmUBG0xou/mfI3L.lzAKR3ErTIYihxRuj/wbigIm",
                UserRole.PROFESSOR,
                Instant.now());
    }

    public static User sampleUserProfessor() {
        User user = new User(
                "moriarty@email.com", "Moriarty", "$2a$12$PxQzW1GGd1bJ8zx.ZI7W3.hhNN./j57OMyuee/dP4W9h9g/G6LMaC");
        user.setRole(UserRole.PROFESSOR);

        return user;
    }

    public static User sampleUserAdmin() {
        User user = new User(
                "admin@email.com", "Administrator", "$2a$12$kSwUVPTzifbCr0KaYTaDB.05Ew.lna7OVBjpDXrBICjsmalRaOliS");
        user.setRole(UserRole.ADMIN);

        return user;
    }

    public static User sampleUserProfessorB() {
        var user = sampleUserProfessor();
        user.setEmail("g.lestrade@email.com");
        user.setName("Gregson Lestrade");

        return user;
    }

    public static User sampleUserProfessorC() {
        var user = sampleUserProfessor();
        user.setEmail("j.capulet@email.com");
        user.setName("Juliet Capulet");

        return user;
    }

    public static User sampleUserStudent() {
        User user = new User(
                "holmes@gmail.com", "Sherlock Holmes", "$2a$12$H43oAFLSYG0SdqhJ9oE2BuG0Sk43lmNFBzD1oUCHMHIE4o2dPVWnC");
        user.setRole(UserRole.STUDENT);

        return user;
    }

    public static User sampleUserStudentB() {
        var user = sampleUserStudent();
        user.setEmail("john.watson@gmail.com");
        user.setName("John Watson");

        return user;
    }
}
