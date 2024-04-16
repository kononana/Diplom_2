package usertest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.GenerateRandomUser;
import user.User;
import user.UserAuthSteps;
import utilities.Endpoints;
import static org.hamcrest.CoreMatchers.equalTo;


public class ModifyUserTest {
    private UserAuthSteps userAuthSteps = new UserAuthSteps();
    private String accessToken;
    private User user;
    private Response response;

    @Before
    public void setUp() {
        RestAssured.baseURI = Endpoints.BASE_URL;
    }

    @After
    public void cleanUp() {
        if (accessToken != null) {
            userAuthSteps.deleteUser(accessToken);
        }
    }
    @Step ("Создание пользователя и получение токена авторизации")
    protected String createUserReceiveToken(){
        user = GenerateRandomUser.createValidRandomUser();
        response = userAuthSteps.createUser(user);
        return response.then().extract().body().path("accessToken");
    }
    @Test
    @DisplayName("Изменение имени и почты пользователя с авторизацией")
    public void changeUserNameAndEmail(){
        accessToken = createUserReceiveToken();
        user.setName("Pavel");
        user.setEmail("fixFake@mail.com");
        response = userAuthSteps.modifyUser(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    public void changeUserPassword(){
        accessToken = createUserReceiveToken();
        user.setPassword("fakePassword");
        response = userAuthSteps.modifyUser(user, accessToken);
        response.then()
                .body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Изменение имени и почты пользователя без авторизации")
    public void changeUserNameAndEmailWithoutAuth(){
        accessToken = createUserReceiveToken();
        user.setName("Petr");
        user.setEmail("fixFake@mail.com");
        response = userAuthSteps.modifyUser(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuth(){
        accessToken = createUserReceiveToken();
        user.setPassword("fakePassword123");
        response = userAuthSteps.modifyUser(user, "");
        response.then()
                .body("success", equalTo(false))
                .and()
                .statusCode(401);
    }
}

