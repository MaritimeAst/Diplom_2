package Clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{

    private static final String CREATE_USER_PATH = "/auth/register";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then();
    }
}
