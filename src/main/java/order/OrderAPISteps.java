package order;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utilities.Endpoints;

import java.util.List;

import static io.restassured.RestAssured.given;


public class OrderAPISteps {
    public static RequestSpecification getRequestSpecific() {
        return given()
                .contentType(ContentType.JSON)
                .baseUri(Endpoints.BASE_URL);
    }

        @Step("Создание заказа с авторизацией")
        public Response createOrderWithToken(Order order, String token) {
            return getRequestSpecific()
                    .header("Authorization", token)
                    .body(order)
                    .post(Endpoints.ORDERS);
        }
        @Step("Создание заказа без авторизации")
        public Response createOrderWithoutToken(Order order) {
            return getRequestSpecific()
                    .body(order)
                    .post(Endpoints.ORDERS);
        }

        @Step("Запрос данных о заказах пользователя по токену")
        public Response getUserOrders(String accessToken) {
            return getRequestSpecific()
                    .header("Authorization", accessToken)
                    .get(Endpoints.ORDERS);
        }

    @Step("Запрос данных о заказах пользователя без авторизации")
    public Response getUserOrderWithoutAuth() {
        return  getRequestSpecific()
                .get(Endpoints.ORDERS);
    }
    @Step("Запрос данных об ингредиентах")
    public Response getIngredients() {
        return  getRequestSpecific()
                .get(Endpoints.INGREDIENTS);
    }
    @Step("Запрос данных об ингредиентах")
    public List<String> getIngredientList() {
        Response response = getIngredients();
        List<String> list = response.jsonPath().getList("data._id");
        return List.of(list.get(0), list.get(2));
    }

    }



