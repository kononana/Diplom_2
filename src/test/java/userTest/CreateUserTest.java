package userTest;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import user.GenerateRandomUser;
import user.User;
import user.UserAuthSteps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CreateUserTest {
    private final UserAuthSteps userSteps = new UserAuthSteps();
    private Response response;
    private User user;
    private String accessToken;

    @Test
    @DisplayName("Проверка создания нового пользователя с указанием обязательных валидных данных")
    public void createUserTestMustBeSuccessful() {
        user = GenerateRandomUser.createValidRandomUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response
                .then().body("accessToken", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка неуспешности создания пользователя с ранее введенными валидными данными")
    public void createSameUserTestMustReturnError() {
        user = GenerateRandomUser.createValidRandomUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .statusCode(200);
        response = userSteps.createUser(user);
        response.then()
                .body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }


    @Test
    @DisplayName("Проверка создания нового пользователя без передачи имени")
    public void createNoNameUserTestMustReturnError() {
        user = GenerateRandomUser.createNoNameUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя без передачи почты")
    public void createNoEmailUserTestMustReturnError() {
        user = GenerateRandomUser.createNoEmailUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Проверка создания нового пользователя без передачи пароля")
    public void createNoPasswordUserTestMustReturnError() {
        user = GenerateRandomUser.createNoPasswordUser();
        response = userSteps.createUser(user);
        accessToken = response
                .then().extract().body().path("accessToken");
        response.then()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void removeToken() {
        if (accessToken != null) {
            userSteps.deleteUser(accessToken);
        }
    }
}