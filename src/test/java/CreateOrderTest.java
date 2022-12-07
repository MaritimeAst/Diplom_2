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
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CreateOrderTest {

    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Создание заказа. Позитивный сценарий")
    @Description("Проверка возможности создания заказа с авторизацией")
    public void createOrderWithAuthAvailable() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken"); //Получение токена из запроса по созданию пользователя, для дальнейшего удаления

        OrderClient orderClient = new OrderClient();
        order = OrderGenerator.getOrderDefault();
        ValidatableResponse responseCreateOrder = orderClient.postOrderAuth(order, accessToken);
        int number = responseCreateOrder.extract().path("order.number");
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("Проверка возможности создания заказа с авторизацией", SC_OK, actualStatusCode);
        MatcherAssert.assertThat("Проверка возможности создания заказа с авторизацией, должен вернуться номер заказа", number, notNullValue());
    }

    @Test
    @DisplayName("Создание заказа. Позитивный сценарий")
    @Description("Проверка возможности создания заказа без авторизации")
    public void createOrderNoAuthAvailable() {

        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken"); //Получение токена из запроса по созданию пользователя, для дальнейшего удаления

        OrderClient orderClient = new OrderClient();
        order = OrderGenerator.getOrderDefault();
        ValidatableResponse responseCreateOrder = orderClient.postOrderNoAuth(order);
        int number = responseCreateOrder.extract().path("order.number");
        int actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("Проверка возможности создания заказа с авторизацией", SC_OK, actualStatusCode);
        MatcherAssert.assertThat("Проверка возможности создания заказа без авторизации, должен вернуться номер заказа", number, notNullValue());
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
