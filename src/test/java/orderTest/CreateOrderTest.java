package orderTest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.OrderAPISteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.UserAuthSteps;
import utilities.Endpoints;
import user.*;
import order.*;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
        private final UserAuthSteps userSteps = new UserAuthSteps();
        private final OrderAPISteps orderSteps = new OrderAPISteps();
        private String accessToken;
        private Response response;
        private Order order;
        private User user;


        @Before
        public void setUp() {
            RestAssured.baseURI = Endpoints.BASE_URL;
        }

        @After
        public void cleanUp() {
            if (accessToken != null) {
                userSteps.deleteUser(accessToken);
            }
        }

    @Step("Получить список валидных ингредиентов")
    protected List<String> ingredientList() {
        response = orderSteps.getIngredients();
        List<String> list = response.jsonPath().getList("data._id");
        return List.of(list.get(0), list.get(4));
    }

    @Step("Получить невалидный список ингредиентов")
    protected List<String> invalidIngredientList() {
            List<String> list = new ArrayList<>();
            list.add("abc");
            list.add("12345");
            return list;
        }
        @Step ("Создание пользователя и получение токена авторизации")
        protected String createUserReceiveToken(){
            user = GenerateRandomUser.createValidRandomUser();
            response = userSteps.createUser(user);
            return response.then().extract().body().path("accessToken");
        }


        @Test
        @DisplayName("Создание заказа с авторизацией пользователя с валидным хэшем ингредиентов")
        public void createOrderWithUserAuthWithValidIngredientHash()  {
            order = new Order(ingredientList());
            accessToken = createUserReceiveToken();
            response = orderSteps.createOrderWithToken(order, accessToken);
            response.then()
                    .body("success", equalTo(true))
                    .and()
                    .statusCode(200);
        }
    @Test
    @DisplayName("Создание заказа без авторизации пользователя с валидным хэшем ингредиентов")
    public void createOrderWithoutUserAuthWithValidIngredientHash() {
        order = new Order(ingredientList());
        response = orderSteps.createOrderWithoutToken(order);
        response.then()
                .statusCode(200)
                .assertThat()
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией пользователя без указания ингредиентов")
    public void createOrderWithUserAuthWithoutValidIngredientHash()  {
        order = new Order();
        accessToken = createUserReceiveToken();
        response = orderSteps.createOrderWithToken(order, accessToken);
        response.then()
                .statusCode(400)
                .assertThat()
                .body("success", equalTo(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

@Test
@DisplayName("Создание заказа без авторизации пользователя, с неверным хэшем ингредиентов")
    public void createOrderWithInvalidIngredientHash() {
            order = new Order(invalidIngredientList());
            response = orderSteps.createOrderWithoutToken(order);
            response.then()
                    .statusCode(500);
}
}

