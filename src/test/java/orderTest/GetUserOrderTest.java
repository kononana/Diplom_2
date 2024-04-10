package orderTest;

import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import order.Order;
import order.OrderAPISteps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.GenerateRandomUser;
import user.User;
import user.UserAuthSteps;
import utilities.Endpoints;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetUserOrderTest {
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

    @Test
    @DisplayName("Получение заказов авторизованного пользователя")
    public void getOrdersForAuthorizedUser() {
        Order order = new Order(ingredientList());
        accessToken = createUserReceiveToken();
        response = orderSteps.createOrderWithToken(order, accessToken);
        response = orderSteps.getUserOrders(accessToken);
        response.then()
                .body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    public void getOrdersForNonAuthorizedUser() {
        response = orderSteps.getUserOrderWithoutAuth();
        response.then()
                .body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Step("получение списка ингредиентов")
    protected List<String> ingredientList() {
        response = orderSteps.getIngredients();
        List<String> list = response.jsonPath().getList("data._id");
        return List.of(list.get(0), list.get(2));
    }

    @Step ("Создание пользователя и получение токена авторизации")
    protected String createUserReceiveToken(){
        user = GenerateRandomUser.createValidRandomUser();
        response = userSteps.createUser(user);
        return response.then().extract().body().path("accessToken");

    }
}