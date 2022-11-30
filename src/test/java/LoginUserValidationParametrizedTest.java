import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Credentials;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class LoginUserValidationParametrizedTest {

    private static String expectedMessage = "email or password are incorrect";
    private UserClient userClient;
    private User user;
    private Credentials credentials;
    private int statusCode;
    private String message;
    private String accessToken;

    public LoginUserValidationParametrizedTest(Credentials credentials, int statusCode, String message) {
        this.credentials = credentials;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] getTestData() {

        return new Object[][]{
                {Credentials.from(UserGenerator.getWithLoginIncorrect()), SC_UNAUTHORIZED, expectedMessage},
                {Credentials.from(UserGenerator.getWithPasswordIncorrect()), SC_UNAUTHORIZED, expectedMessage},
                {Credentials.from(UserGenerator.getWithoutLogin()), SC_UNAUTHORIZED, expectedMessage},
                {Credentials.from(UserGenerator.getWithoutPassword()), SC_UNAUTHORIZED, expectedMessage}
        };
    }

    @DisplayName("Логин пользователем. Негативный параметризованный тест")
    @Description("Проверка корректности валидации вызова метода логина, если неправильно указать логин или пароль, если не указан логин или пароль")
    @Test
    public void loginUserWithError() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();

        userClient.create(user);                                                      // Вызов метода создания пользователя
        ValidatableResponse responseLogin = userClient.login(Credentials.from(user)); // В переменной сохраняется результат вызова метода логина пользователя

        accessToken = responseLogin.extract().path("accessToken");

        ValidatableResponse responseLoginWithError = userClient.login(credentials); // В переменной сохраняется результат вызова метода логина пользователя
        String actualMessage = responseLoginWithError.extract().path("message");
        int actualStatusCode = responseLoginWithError.extract().statusCode();

        assertEquals("Проверка корректности возвращаемого кода ошибки, при выполнении входа пользователем с некорректными кредами", statusCode, actualStatusCode);
        assertEquals("Проверка корректности возвращаемого текста ошибки, при выполнении входа пользователем с некорректными кредами", message, actualMessage);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}