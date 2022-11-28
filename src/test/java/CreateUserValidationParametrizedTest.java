import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserValidationParametrizedTest {

    private UserClient userClient;
    private User user;
    private int statusCode;
    private String message;

    public CreateUserValidationParametrizedTest(User user, int statusCode, String message) {
        this.user = user;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] getTestData() {

        return new Object[][]{
                {UserGenerator.getWithoutLogin(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.getWithoutPassword(), SC_FORBIDDEN, "Email, password and name are required fields"},
                {UserGenerator.getWithoutName(), SC_FORBIDDEN, "Email, password and name are required fields"}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    @DisplayName("Валидация метода создания пользователя. Негативный сценарий")
    @Description("Проверка, что если не указать логин, пароль или имя, создание пользователя завершается ошибкой")
    public void courierCreationWithoutRequiredFieldsThenError() {

        UserClient userClient = new UserClient();
        ValidatableResponse responseCreate = userClient.create(user);                 // В переменной сохраняется результат вызова метода создания курьера

        String actualMessage = responseCreate.extract().path("message");
        int actualStatusCode = responseCreate.extract().statusCode();

        assertEquals("Проверка корректности возвращаемого кода ошибки, при создании курьера без обязательных полей", statusCode, actualStatusCode);
        assertEquals("Проверка корректности возвращаемого текста ошибки, при создании курьера без обязательных полей", message, actualMessage);
    }
}
