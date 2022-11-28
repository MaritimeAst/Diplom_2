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

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class CreateOrderValidationTest {

    private OrderClient orderClient;
    private Order order;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private String expectedMessage = "One or more ids provided are incorrect";

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = UserGenerator.getDefault();
        ValidatableResponse responseCreate = userClient.create(user);
        accessToken = responseCreate.extract().path("accessToken"); //Получение токена из запроса по созданию пользователя, для дальнейшего удаления
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов. Негативный сценарий")
    @Description("Проверка невозможности создания заказа без ингредиентов")
    public void createOrderWithoutIngredients() {

        OrderClient orderClient = new OrderClient();
        order = OrderGenerator.getOrderWithoutIngredients();
        ValidatableResponse responseCreateOrder = orderClient.postOrderAuth(order, accessToken);

        int actualStatusCode = responseCreateOrder.extract().statusCode();
        assertEquals("Проверка невозможности создания заказа без ингредиентов", SC_INTERNAL_SERVER_ERROR, actualStatusCode);

    }

    @Test
    @DisplayName("Создание заказа с неправильным хэшем ингредиентов. Негативный сценарий")
    @Description("Проверка невозможности создания заказа с неправильным хэшем ингредиентов")
    public void createOrderNoAuthAvailable() {

        OrderClient orderClient = new OrderClient();
        order = OrderGenerator.getOrderWithWrongIngredients();
        ValidatableResponse responseCreateOrder = orderClient.postOrderAuth(order, accessToken);

        int actualStatusCode = responseCreateOrder.extract().statusCode();
        String actualMessage = responseCreateOrder.extract().path("message");
        assertEquals("Проверка невозможности создания заказа с неправильным хэшем ингредиентов", SC_BAD_REQUEST, actualStatusCode);
        assertEquals("Проверка невозможности создания заказа с неправильным хэшем ингредиентов", expectedMessage, actualMessage);

    }

    @After
    public void cleanUp() {
        userClient.delete(accessToken);
    }
}
