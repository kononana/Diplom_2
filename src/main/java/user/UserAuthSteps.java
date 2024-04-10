package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.Endpoints;

import static io.restassured.RestAssured.given;

public class UserAuthSteps {
    public static RequestSpecification getRequestSpec() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Endpoints.BASE_URL);
    }

    @Step("Регистрация нового пользователя")
    public Response createUser(User user) {
        return getRequestSpec()
                .body(user)
                .post(Endpoints.REGISTER);
    }

    @Step("Удаление профиля пользователя")
    public void deleteUser(String accessToken) {
        getRequestSpec()
                .header("Authorization", accessToken)
                .delete(Endpoints.USER);
    }


    @Step("Авторизация пользователя по логину и токену")
    public Response userLoginWithToken(User user, String accessToken) {
        return getRequestSpec()
                .header("Authorization", accessToken)
                .body(user)
                .post(Endpoints.LOGIN);
    }

    @Step("Изменение профиля пользователя")
    public Response modifyUser(User user, String accessToken) {
        return getRequestSpec()
                .header("Authorization", accessToken)
                .body(user)
                .patch(Endpoints.USER);
    }
}



