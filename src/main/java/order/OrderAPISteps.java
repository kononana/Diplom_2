package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import utilities.Endpoints;

import static io.restassured.RestAssured.given;


public class OrderAPISteps {

        @Step("Создание заказа с авторизацией")
        public Response createOrderWithToken(Order order, String token) {
            return given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", token)
                    .baseUri(Endpoints.BASE_URL)
                    .body(order)
                    .post(Endpoints.ORDERS);
        }
        @Step("Создание заказа без авторизации")
        public Response createOrderWithoutToken(Order order) {
            return given()
                    .header("Content-Type", "application/json")
                    .baseUri(Endpoints.BASE_URL)
                    .body(order)
                    .post(Endpoints.ORDERS);
        }


        @Step("Запрос данных о заказах пользователя по токену")
        public Response getUserOrders(String accessToken) {
            return given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", accessToken)
                    .baseUri(Endpoints.BASE_URL)
                    .get(Endpoints.ORDERS);
        }

    @Step("Запрос данных о заказах пользователя без авторизации")
    public Response getUserOrderWithoutAuth() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .get(Endpoints.ORDERS);
    }
    @Step("Запрос данных об ингредиентах")
    public Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .baseUri(Endpoints.BASE_URL)
                .get(Endpoints.INGREDIENTS);
    }

    }



