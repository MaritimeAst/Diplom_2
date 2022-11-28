package generators;

import models.User;

public class UserGenerator {
    private static String email = "TestUser" + (int) (Math.random() * 1000) + "@yandex.ru";
    private static String emailChanged = "TestUserChanged" + (int) (Math.random() * 1000) + "@yandex.ru";

    public static User getDefault() {
        return new User(email, "password", "TestUser");
    }

    //тестовые данные для негативных кейсов по регистрации пользователя
    public static User getWithoutLogin() {
        return new User(null, "password", "TestUser");
    }
    public static User getWithoutPassword() {
        return new User(email, null, "TestUser");
    }
    public static User getWithoutName() {
        return new User(email, "password", null);
    }

    //тестовые данные для негативных кейсов входа пользователя
    public static User getWithLoginIncorrect() {
        return new User(email + 1, "password", null);
    }
    public static User getWithPasswordIncorrect() {
        return new User(email, "password1", null);
    }

    //тестовые данные для изменения пользователя
    public static User getUserChangeEmail() {
        return new User(emailChanged, "password", "TestUser");
    }
    public static User getUserChangePassword() {
        return new User(email, "passwordChanged", "TestUser");
    }
    public static User getUserChangeName() {
        return new User(email, "password", "TestUserChanged");
    }
}
