import clients.OrderClient;
import clients.UserClient;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class GetOrdersListValidationTest {

    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private String expectedMessage = "You should be authorised";

    @Test
    @DisplayName("Получение списка заказов пользователя. Негативный сценарий")
    @Description("Проверка невозможности получения списка заказов пользователя без указания токена авторизации")
    public void getOrdersListNoAuth() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreateUser = userClient.create(user);                        //Создание нового пользователя
        accessToken = responseCreateUser.extract().path("accessToken");                       //Получение токена для дальнейшей авторизации и удаления

        OrderClient orderClient = new OrderClient();

        ValidatableResponse responseGetOrdersList = orderClient.getOrdersNoAuth();          // Получение списка заказов созданного пользователя без указания токена авторизации

        int actualStatusCode = responseGetOrdersList.extract().statusCode();
        Boolean success = responseGetOrdersList.extract().path("success");
        String actualMessage = responseGetOrdersList.extract().path("message");

        assertEquals("Проверка невозможности получения списка заказов без указания токена авторизации", SC_UNAUTHORIZED, actualStatusCode);
        assertFalse("Проверка невозможности получения списка заказов без указания токена авторизации", success);
        assertEquals("Проверка невозможности получения списка заказов без указания токена авторизации", expectedMessage, actualMessage);
    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
