
import Clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CreateUserTest {

    private UserClient courierClient;
    private User user;
    private int id;

    @Before
    public void setUp() {
        courierClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @Test
    @DisplayName("Создание пользователя. Позитивный тест")
    @Description("Проверка, что пользователя можно создать")
    public void userCanBeCreated() {

        User user = UserGenerator.getDefault();
        UserClient courierClient = new UserClient();

        ValidatableResponse responseCreate = courierClient.create(user);                 // В переменной сохраняется результат вызова метода создания пользователя
        boolean isUserCreated = responseCreate.extract().path("true");


        assertTrue("Проверка возможности создания курьера", isUserCreated);
    }
}
