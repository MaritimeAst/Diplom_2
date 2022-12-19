import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Credentials;
import models.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class UserDataChangeParametrizedTest {

    private UserClient userClient;
    private User user;
    private User userChanged;
    private String accessToken;
    private int statusCode;

    public UserDataChangeParametrizedTest(User userChanged, int statusCode) {
        this.userChanged = userChanged;
        this.statusCode = statusCode;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] getTestData() {

        return new Object[][]{
                {UserGenerator.getUserChangeEmail(), SC_OK},
                {UserGenerator.getUserChangePassword(), SC_OK},
                {UserGenerator.getUserChangeName(), SC_OK}
        };
    }

    @DisplayName("Изменение данных пользователя. Позитивный тест")
    @Description("Проверка возможности изменения данных пользователя, вход с изменеными кредами")
    @Test
    public void userDataChangeAvailable() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreateUser = userClient.create(user);
        accessToken = responseCreateUser.extract().path("accessToken");                                  //Получение токена из запроса по созданию пользователя, для дальнейшего использования в запросе изменения данных пользователя и удаления

        ValidatableResponse responseUserDataChange = userClient.userDataChange(accessToken, userChanged);  //В переменной сохраняется результат вызова метода изменения данных пользователя

        int actualStatusCode = responseUserDataChange.extract().statusCode();                               //Статус-код вызова метода изменения данных пользователя
        String emailChanged = responseUserDataChange.extract().path("user.email");
        String nameChanged = responseUserDataChange.extract().path("user.name");

        ValidatableResponse responseLogin = userClient.login(Credentials.from(userChanged));               // В переменной сохраняется результат вызова метода логина пользователя c измененными данными
        int loginStatusCode = responseLogin.extract().statusCode();

        assertEquals("Проверка возможности изменения данных пользователя", SC_OK, actualStatusCode);
        assertEquals("Проверка возможности входа с изменененными данными", SC_OK, loginStatusCode);
        assertEquals("Проверка на то, что значение email, переданное в запросе по изменению данных пользователя, успешно сохранилось", userChanged.getEmail().toLowerCase(), emailChanged);
        assertEquals("Проверка на то, что значение name, переданное в запросе по изменению данных пользователя, успешно сохранилось", userChanged.getName(), nameChanged);
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
