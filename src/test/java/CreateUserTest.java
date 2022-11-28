import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CreateUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private String refreshToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @Test
    @DisplayName("Создание пользователя. Позитивный тест")
    @Description("Проверка, что пользователя можно создать")
    public void userCanBeCreated() {

        User user = UserGenerator.getDefault();
        UserClient userClient = new UserClient();

        ValidatableResponse responseCreate = userClient.create(user);                 // В переменной сохраняется результат вызова метода создания пользователя

        accessToken = responseCreate.extract().path("accessToken");
        System.out.println(accessToken);
        refreshToken = responseCreate.extract().path("refreshToken");
        System.out.println(refreshToken);
        boolean isUserCreated = responseCreate.extract().path("success");
        System.out.println(isUserCreated);


        assertTrue("Проверка возможности создания пользователя", isUserCreated);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
