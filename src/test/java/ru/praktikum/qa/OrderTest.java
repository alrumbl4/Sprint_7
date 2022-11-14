package ru.praktikum.qa;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.praktikum.qa.order.Order;
import ru.praktikum.qa.order.OrderClient;

import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTest {

    private OrderClient orderClient;
    private int getTrack;
    private final String firstName;
    private final String lastName;
    private final String address;
    private final String metroStation;
    private final String phone;
    private final Number rentTime;
    private final String deliveryDate;
    private final String comment;
    private final String[] color;

    public OrderTest(String firstName, String lastName, String address, String metroStation, String phone, Number rentTime, String deliveryDate, String comment, String[] color) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.metroStation = metroStation;
        this.phone = phone;
        this.rentTime = rentTime;
        this.deliveryDate = deliveryDate;
        this.comment = comment;
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] OrderCreateTestParamApiTestData() {
        return new Object[][] {
                {
                        "Alexey",
                        "Rumyantsev",
                        "Moscow",
                        "Yellow",
                        "+79999999999",
                        1,
                        "2022-11-14",
                        "No comment",
                        new String[]{"BLACK"},
                },
                {
                        "Alexey",
                        "Rumyantsev",
                        "Moscow",
                        "Yellow",
                        "+79999999999",
                        2,
                        "2022-11-14",
                        "No comment",
                        new String[]{"BLACK", "GREY"},
                },
                {
                        "Alexey",
                        "Rumyantsev",
                        "Moscow",
                        "Yellow",
                        "+79999999999",
                        3,
                        "2022-11-14",
                        "No comment",
                        new String[]{},
                },

        };
    }


    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @After
    public void cancelOrder() {
        orderClient.cancelOrder(getTrack);
    }


    @Test
    @DisplayName("Создание заказа")
    @Description("Проверяем возможность создать новый заказ в системе")
    public void createOrder() {
        Order order = new Order(firstName, lastName, address, metroStation, phone, rentTime, deliveryDate, comment, color);

        ValidatableResponse validatableResponse = orderClient.createOrder(order);
        validatableResponse.assertThat().body("track", notNullValue()).and().statusCode(201);
        getTrack = validatableResponse.extract().body().path("track");
    }
}
