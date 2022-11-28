import clients.OrderClient;
import clients.UserClient;
import generators.OrderGenerator;
import generators.UserGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import models.Order;
import models.User;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class GetOrdersListTest {

    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
    }

    @Test
    @DisplayName("Получение списка заказов пользователя. Позитивный сценарий")
    @Description("Проверка возможности получения списка заказов при использовании токена авторизации")
    public void getOrdersListWithAuth() {

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

//    @Test
//    @DisplayName("Создание заказа. Позитивный сценарий")
//    @Description("Проверка возможности создания заказа без авторизации")
//    public void createOrderNoAuthAvailable() {
//
//        OrderClient orderClient = new OrderClient();
//        order = OrderGenerator.getOrderDefault();
//        ValidatableResponse responseCreateOrder = orderClient.postOrderNoAuth(order);
//        int number = responseCreateOrder.extract().path("order.number");
//        int actualStatusCode = responseCreateOrder.extract().statusCode();
//        assertEquals("Проверка возможности создания заказа с авторизацией", SC_OK, actualStatusCode);
//        MatcherAssert.assertThat("Проверка возможности создания заказа без авторизации, должен вернуться номер заказа", number, notNullValue());
//    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
