import clients.UserClient;
import com.google.gson.JsonObject;
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

public class UserDataChangeTest {

    private UserClient userClient;
    private User user;
    private String accessToken;


    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @DisplayName("Изменение данных пользователя. Позитивный тест")
    @Description("Проверка возможности изменения данных пользователя, вход с изменеными логином и паролем")
    @Test
    public void userDataChangeAvailable() {

        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken");                             //Получение токена из запроса по созданию пользователя, для дальнейшего удаления

        User user = UserGenerator.getUserChangeEmail();

        ValidatableResponse responseUserDataChange = userClient.userDataChange(accessToken, user);  //В переменной сохраняется результат вызова метода изменения данных пользователя
        int actualStatusCode = responseUserDataChange.extract().statusCode();                       //Статус-код вызова метода изменения данных пользователя
        String emailChanged = responseUserDataChange.extract().path("user.email");
        String nameChanged = responseUserDataChange.extract().path("user.name");

        ValidatableResponse responseLogin = userClient.login(Credentials.from(user));               // В переменной сохраняется результат вызова метода логина пользователя
        int loginStatusCode = responseLogin.extract().statusCode();

        assertEquals("Проверка возможности изменения данных пользователя", SC_OK, actualStatusCode);
        assertEquals("Проверка возможности входа с изменененными данными", SC_OK, loginStatusCode);
        assertEquals("Проверка на то, что значение email, переданное в запросе по изменению данных пользователя, успешно сохранилось", user.getEmail().toLowerCase(), emailChanged);
        assertEquals("Проверка на то, что значение name, переданное в запросе по изменению данных пользователя, успешно сохранилось", user.getName(), nameChanged);

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }

}
