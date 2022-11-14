package ru.praktikum.qa;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.praktikum.qa.courier.Courier;
import ru.praktikum.qa.courier.CourierClient;
import ru.praktikum.qa.courier.Credentials;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierTest {

    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @After
    public void deleteCourier() {
        courierClient.deleteCourier(courierId);
    }

    @Test
    @DisplayName("Создание курьера")
    @Description("Проверяем возможность регистрации нового курьера в системе")
    public void createCourier() {
        Courier courier = new Courier("Grek", "112233", "Alexey");

        ValidatableResponse validatableResponse = courierClient.createCourier(courier);
        validatableResponse.assertThat().body("ok", equalTo(true)).and().statusCode(201);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Проверяем, что в системе нельзя создать двух курьеров с одинаковыми входными данными")
    public void createCourierTwiceSameLogin() {
        Courier courier = new Courier("Grek", "112233", "Alexey");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.createCourier(courier);
        validatableResponse.assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой.")).and().statusCode(409);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Создание курьера без заполнения обязательных полей")
    @Description("Проверяем возможность регистрации нового курьера без заполнения поля 'login'")
    public void creatingCourierWithoutFieldLogin() {
        Courier courier = new Courier("", "112233", "Alexey");

        ValidatableResponse validatableResponse = courierClient.createCourier(courier);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без заполнения обязательных полей")
    @Description("Проверяем возможность регистрации нового курьера без заполнения поля 'password'")
    public void creatingCourierWithoutFieldPassword() {
        Courier courier = new Courier("Grek", "", "Alexey");

        ValidatableResponse validatableResponse = courierClient.createCourier(courier);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }

    @Test
    @DisplayName("Создание курьера без заполнения обязательных полей")
    @Description("Проверяем возможность регистрации нового курьера без заполнения поля 'lastName'")
    public void creatingCourierWithoutFieldLastName() {
        Courier courier = new Courier("Grek", "112233", "");

        ValidatableResponse validatableResponse = courierClient.createCourier(courier);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи")).and().statusCode(400);
    }
}