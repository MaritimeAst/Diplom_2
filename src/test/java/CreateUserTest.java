import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class CreateUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Создание пользователя. Позитивный тест")
    @Description("Проверка, что пользователя можно создать")
    public void userCanBeCreated() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();

        ValidatableResponse responseCreateUser = userClient.create(user);                 // В переменной сохраняется результат вызова метода создания пользователя

        accessToken = responseCreateUser.extract().path("accessToken");

        boolean isUserCreated = responseCreateUser.extract().path("success");
        String user = responseCreateUser.extract().path("user.email");

        assertTrue("Проверка возможности создания пользователя", isUserCreated);
        MatcherAssert.assertThat("Проверка возможности создания пользователя, в ответе должно содержаться поле user", user, notNullValue());
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
        try {                                           //Задержка добавлена для пердотвращения появления ошибки 429
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
