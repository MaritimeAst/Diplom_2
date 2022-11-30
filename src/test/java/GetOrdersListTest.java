import clients.OrderClient;
import clients.UserClient;
import generators.OrderGenerator;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetOrdersListTest {

    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Получение списка заказов пользователя. Позитивный сценарий")
    @Description("Проверка возможности получения списка заказов при использовании токена авторизации")
    public void getOrdersListWithAuth() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();

        ValidatableResponse responseCreate = userClient.create(user);                        //Создание нового пользователя
        accessToken = responseCreate.extract().path("accessToken");                       //Получение токена для дальнейшей авторизации и удаления

        OrderClient orderClient = new OrderClient();
        order = OrderGenerator.getOrderDefault();
        orderClient.postOrderAuth(order, accessToken);                                      //Создание заказа

        ValidatableResponse responseGetOrdersList = orderClient.getOrdersAuth(accessToken); // Получение списка заказов созданного пользователя
        int actualStatusCode = responseGetOrdersList.extract().statusCode();
        Boolean success = responseGetOrdersList.extract().path("success");

        assertEquals("Проверка возможности получения списка заказов при использовании токена авторизации", SC_OK, actualStatusCode);
        assertTrue("Проверка возможности получения списка заказов при использовании токена авторизации", success);
    }


    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
