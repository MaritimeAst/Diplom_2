package clients;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Credentials;
import models.User;

import static io.restassured.RestAssured.given;

public class UserClient extends Client{

    private static final String CREATE_USER_PATH = "/auth/register";
    private static final String LOGIN_USER_PATH = "/auth/login";
    private static final String DELETE_USER_PATH = "/auth/user";


    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .log().all()
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .log().all();
    }

    @Step("Логин с кредами пользователя")
    public ValidatableResponse login(Credentials credentials){
        return given()
                .spec(getSpec())
                .log().all()
                .body(credentials)
                .when()
                .post(LOGIN_USER_PATH)
                .then()
                .log().all();
    }

    @Step("Изменение пользователя")
    public ValidatableResponse userDataChange(String accessToken, User user) {
        return given()
                .spec(getSpec())
                .log().all()
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch(DELETE_USER_PATH)
                .then()
                .log().all();
    }


    @Step("Удаление пользователя")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getSpec())
                .log().all()
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_USER_PATH)
                .then()
                .log().all()
                .statusCode(202);
    }
}
