package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client  {
    private static final String ORDER_PATH = "/orders";

    @Step("Создание заказа c авторизацией")
    public ValidatableResponse postOrderAuth(Order order, String accessToken) {
        return given()
                .spec(getSpec())
                .log().all()
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Создание заказа без авторизациии")
    public ValidatableResponse postOrderNoAuth(Order order) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов пользователя с авторизацией")
    public ValidatableResponse getOrdersAuth(String accessToken) {
        return given()
                .spec(getSpec())
                .log().all()
                .header("Authorization", accessToken)
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов пользователя без авторизации")
    public ValidatableResponse getOrdersNoAuth() {
        return given()
                .spec(getSpec())
                .log().all()
                .when()
                .get(ORDER_PATH)
                .then();
    }


}
