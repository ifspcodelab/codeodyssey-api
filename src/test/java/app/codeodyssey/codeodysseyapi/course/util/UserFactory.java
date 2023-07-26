package app.codeodyssey.codeodysseyapi.course.util;

import app.codeodyssey.codeodysseyapi.user.data.User;

public final class UserFactory {
    public static User createValidUser() {
        return new User("UserName", "Email", "Password");
    }
}
