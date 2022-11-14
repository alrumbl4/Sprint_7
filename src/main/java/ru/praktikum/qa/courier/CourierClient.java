package ru.praktikum.qa.courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends ru.praktikum.qa.courier.Client {

    private static final String COURIER_PATH = "/api/v1/courier/";
    private static final String COURIER_LOGIN_PATH = "/api/v1/courier/login";

    @Step("Создание курьера")
    public ValidatableResponse createCourier(ru.praktikum.qa.courier.Courier courier) {
        return given()
                .spec(getSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Получение id курьера в системе")
    public ValidatableResponse getId(Credentials credentials) {
        return given()
                .spec(getSpec())
                .body(credentials)
                .when()
                .post(COURIER_LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера")
    public ValidatableResponse deleteCourier(int id) {
        return  given()
                .spec(getSpec())
                .when()
                .delete(COURIER_PATH + id)
                .then();
    }
}
