import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CreateUserValidationParametrizedTest {

    private UserClient userClient;
    private User user;
    private int statusCode;
    private String message;
    private String accessToken;

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

    @Test
    @DisplayName("Валидация метода создания пользователя. Негативный сценарий")
    @Description("Проверка, что если не указать логин, пароль или имя, создание пользователя завершается ошибкой")
    public void courierCreationWithoutRequiredFieldsThenError() {

        userClient = new UserClient();
        ValidatableResponse responseCreateUser = userClient.create(user);                 // В переменной сохраняется результат вызова метода создания пользователя

        String actualMessage = responseCreateUser.extract().path("message");
        int actualStatusCode = responseCreateUser.extract().statusCode();

        if (actualStatusCode == SC_OK) {                                                  // Если пользователь будет создан (по причине какого-либо бага), то будет выполнено его удаление. Тест выдаст ошибку
            accessToken = responseCreateUser.extract().path("accessToken");
            userClient.delete(accessToken);
        }

        assertEquals("Проверка корректности возвращаемого кода ошибки, при создании курьера без обязательных полей", statusCode, actualStatusCode);
        assertEquals("Проверка корректности возвращаемого текста ошибки, при создании курьера без обязательных полей", message, actualMessage);

        try {                                           //Задержка добавлена для пердотвращения появления ошибки 429
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
