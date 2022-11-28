import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UserDataChangeValidationParametrizedTest {

    private UserClient userClient;
    private User user;
    private User userChanged;
    private String accessToken;
    private static String expectedMessage = "You should be authorised";
    private int statusCode;
    private String message;

    public UserDataChangeValidationParametrizedTest(User userChanged, int statusCode, String message) {
        this.userChanged = userChanged;
        this.statusCode = statusCode;
        this.message = message;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] getTestData() {

        return new Object[][]{
                {UserGenerator.getUserChangeEmail(), SC_UNAUTHORIZED, expectedMessage},
                {UserGenerator.getUserChangePassword(), SC_UNAUTHORIZED, expectedMessage},
                {UserGenerator.getUserChangeName(), SC_UNAUTHORIZED, expectedMessage}
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");                                 //Получение токена из запроса по созданию пользователя, для дальнейшего удаления
    }

    @DisplayName("Изменение данных пользователя без токена авторизации. Негативный сценарий")
    @Description("Проверка невозможности изменения данных пользователя, если в запросе не передан токен авторизации")
    @Test
    public void userDataChangeWithoutToken() {

        ValidatableResponse responseUserDataChange = userClient.userDataChange("WrongToken", userChanged);  //В переменной сохраняется результат вызова метода изменения данных пользователя
        int actualStatusCode = responseUserDataChange.extract().statusCode();                           //Статус-код вызова метода изменения данных пользователя
        String actualMessage = responseUserDataChange.extract().path("message");

        assertEquals("Проверка возможности изменения данных пользователя", SC_UNAUTHORIZED, actualStatusCode);
        assertEquals("Проверка текста сообщения, если в запросе по изменению данных пользователя не передан токен", expectedMessage, actualMessage);

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
