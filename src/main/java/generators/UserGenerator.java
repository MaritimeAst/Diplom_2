package generators;

import models.User;

public class UserGenerator {
    private static String email = "TestUser" + (int) (Math.random() * 1000) + "@yandex.ru";
    private static String emailChanged = "TestUserChanged" + (int) (Math.random() * 1000) + "@yandex.ru";

    public static User getDefault() {
        return new User(email, "password", "TestUser");
    }

    public static User getWithoutLogin() {
        return new User(null, "password", "TestUser");
    }
    public static User getWithoutPassword() {
        return new User(email, null, "TestUser");
    }
    public static User getWithoutName() {
        return new User(email, "password", null);
    }

    public static User getWithLoginIncorrect() {
        return new User(email + 1, "password", null);
    }
    public static User getWithPasswordIncorrect() {
        return new User(email, "password1", null);
    }

    public static User getUserChangeEmail() {
        return new User(emailChanged, "passwordChanged", "TestUserNameChanged");
    }
}
