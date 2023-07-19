package app.codeodyssey.codeodysseyapi.user.data;

public final class UserFactory {
    private UserFactory() {}

    public static User sampleUserProfessor() {
        User user = new User(
                "moriarty@email.com", "Moriarty", "$2a$12$PxQzW1GGd1bJ8zx.ZI7W3.hhNN./j57OMyuee/dP4W9h9g/G6LMaC");
        user.setRole(UserRole.PROFESSOR);

        return user;
    }
}
