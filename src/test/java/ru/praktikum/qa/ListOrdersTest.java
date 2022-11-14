package ru.praktikum.qa;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.qa.order.OrderClient;

import static org.hamcrest.CoreMatchers.notNullValue;

public class ListOrdersTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Получение списка заказов без указания courierId")
    public void getListOrders() {

        ValidatableResponse validatableResponse = orderClient.getListOrder();
        validatableResponse.assertThat().body("orders",notNullValue()).and().statusCode(200);
    }
}
