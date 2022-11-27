package generators;

import models.User;

public class UserGenerator {
    private static String email = "TestUser" + (int) (Math.random() * 1000) + "@yandex.ru";

    public static User getDefault() {
        return new User(email, "password", "TestUser");
    }
}
