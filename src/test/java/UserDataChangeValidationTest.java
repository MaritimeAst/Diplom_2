import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;

public class UserDataChangeValidationTest {

    private UserClient userClient;
    private User user;
    private String accessToken;
    private String expectedMessage = "You should be authorised";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @DisplayName("Изменение данных пользователя без токена авторизации. Негативный сценарий")
    @Description("Проверка невозможности изменения данных пользователя, если в запросе не передан токен авторизации")
    @Test
    public void userDataChangeWithoutToken() {

        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");                                 //Получение токена из запроса по созданию пользователя, для дальнейшего удаления

        User user = UserGenerator.getUserChangeEmail();

        ValidatableResponse responseUserDataChange = userClient.userDataChange(null, user);  //В переменной сохраняется результат вызова метода изменения данных пользователя
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
