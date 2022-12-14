import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.junit.Assert.assertEquals;

public class CreateUserValidationTest {

    private UserClient userClient;
    private User user;
    private String accessToken;

    private int statusCode = SC_FORBIDDEN;
    private String message = "User already exists";

    @Test
    @DisplayName("Повторное создание пользователя. Негативный сценарий")
    @Description("Проверка, что нельзя создать двух одинаковых пользователей")
    public void userCreationWithUsedLoginThenError() {

        userClient = new UserClient();
        User user = UserGenerator.getDefault();

        ValidatableResponse responseFirstCreate = userClient.create(user);
        ValidatableResponse responseSecondCreate = userClient.create(user);                 // В переменной сохраняется результат вызова метода создания пользователя

        accessToken = responseFirstCreate.extract().path("accessToken");
        System.out.println(accessToken);

        String actualMessage = responseSecondCreate.extract().path("message");
        int actualStatusCode = responseSecondCreate.extract().statusCode();

        assertEquals("Проверка корректности возвращаемого кода ошибки, при попытке создать пользователя с логином, который уже есть в системе", statusCode, actualStatusCode);
        assertEquals("Проверка корректности возвращаемого текста ошибки, при попытке создать пользователя с логином, который уже есть в системе", message, actualMessage);
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
