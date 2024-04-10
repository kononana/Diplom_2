package userTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.GenerateRandomUser;
import user.User;
import user.UserAuthSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTest {
    private final UserAuthSteps userSteps = new UserAuthSteps();
    private Response response;
    private Response login;
    private User user;
    private String accessToken;

    @Before
    public void setUp(){
        user = GenerateRandomUser.createValidRandomUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");

    }

    @Test
    @DisplayName("Авторизация пользователя с валидными данными")
    public void LoginUserSuccessful() {
        login = userSteps.userLoginWithToken(user, accessToken);
        login.then()
                .body("accessToken", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Авторизация пользователя с невалидными данными")
    public void LoginUserWithoutToken1() {
        // Устанавливаем невалидные данные
        user.setEmail("fake@mail.com");
        user.setPassword("123abc");
        login = userSteps.userLoginWithToken(user, accessToken);
        login.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }

    @After
    public void removeToken() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }

    }
}

