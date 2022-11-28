import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import models.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

public class LoginUserTest {

    private UserClient userClient;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @DisplayName("Логин пользователем. Позитивный тест")
    @Description("Проверка возможности входа с корректными данными, если переданные обязательные поля")
    @Test
    public void userLoginAvailable() {

        User user = UserGenerator.getDefault();
        UserClient userClient = new UserClient();
        userClient.create(user);                                                      // Вызов метода создания пользователя
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user)); // В переменной сохраняется результат вызова метода логина пользователя

                                                // В переменной сохраняется id созданного курьера
        int actualStatusCode = responseLogin.extract().statusCode();
        accessToken = responseLogin.extract().path("accessToken");

        assertEquals("Проверка возможности логина", SC_OK, actualStatusCode);

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
