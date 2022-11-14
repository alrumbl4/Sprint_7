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
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierTest {

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
    @DisplayName("Авторизация курьера")
    @Description("Проверяем, что после успешной регистрации курьер может авторизоваться в системе")
    public void authorizationCourier() {
        Courier courier = new Courier("Grek", "112233", "Alexey");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.getId(Credentials.form(courier));
        validatableResponse.assertThat().body("id", notNullValue()).and().statusCode(200);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация курьера без заполнения обязательных полей")
    @Description("Проверяем возможность авторизоваться в системе без заполнения поля 'login'")
    public void authorizationCourierWithoutLogin() {
        Courier courier = new Courier("Grek", "112233", "Alexey");
        Credentials credentials = new Credentials("", "112233");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.getId(credentials);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация курьера без заполнения обязательных полей")
    @Description("Проверяем возможность авторизоваться в системе без заполнения поля 'password'")
    public void authorizationCourierWithoutPassword() {
        Courier courier = new Courier("Grek", "112233", "Alexey");
        Credentials credentials = new Credentials("Grek", "");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.getId(credentials);
        validatableResponse.assertThat().body("message", equalTo("Недостаточно данных для входа")).and().statusCode(400);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация с неверными входными данными")
    @Description("Проверить, что система вернёт ошибку, если при авторизации неправильно указать логин")
    public void authorizationWithInvalidLogin() {
        Courier courier = new Courier("Grek", "112233","Alexey");
        Credentials credentials = new Credentials("Lol", "112233");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.getId(credentials);
        validatableResponse.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация с неверными входными данными")
    @Description("Проверить, что система вернёт ошибку, если при авторизации неправильно указать пароль")
    public void authorizationWithInvalidPassword() {
        Courier courier = new Courier("Grek", "112233","Alexey");
        Credentials credentials = new Credentials("Grek", "12345");

        courierClient.createCourier(courier);
        ValidatableResponse validatableResponse = courierClient.getId(credentials);
        validatableResponse.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);

        courierId = courierClient.getId(Credentials.form(courier)).extract().path("id");
    }

    @Test
    @DisplayName("Авторизация под несуществующим пользователем")
    @Description("Проверяем, что в систему невозможно авторизоваться без предварительно выполненной успешной регистрации")
    public void authorizationWithoutRegistration() {
        Courier courier = new Courier("Grek", "112233", "Alexey");

        ValidatableResponse validatableResponse = courierClient.getId(Credentials.form(courier));
        validatableResponse.assertThat().body("message", equalTo("Учетная запись не найдена")).and().statusCode(404);
    }
}
