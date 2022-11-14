package ru.praktikum.qa.order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.qa.courier.Client;

import static io.restassured.RestAssured.given;

public class OrderClient extends Client {

    private static final String ORDER_PATH = "/api/v1/orders/";
    private static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .spec(getSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получение списки заказов")
    public ValidatableResponse getListOrder() {
        return given()
                .spec(getSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Отмена заказа")
    public ValidatableResponse cancelOrder(int getTrack) {
        return given()
                .spec(getSpec())
                .when()
                .queryParam("track", getTrack)
                .put(CANCEL_ORDER_PATH)
                .then();
    }
}
