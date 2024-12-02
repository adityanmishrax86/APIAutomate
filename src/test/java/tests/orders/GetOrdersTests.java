package tests.orders;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.carts.CartModel;
import framework.models.games.GameResponseModel;
import framework.models.generic.ErrorResponseModel;
import framework.models.orders.OrderModel;
import framework.models.orders.OrdersResponseModel;
import framework.utils.AuthenticationUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import shared.SharedCart;
import shared.SharedOrders;
import shared.SharedUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetOrdersTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Orders for the User")
    @Order(1)
    public void AddOrders() {
        Map<String, Object> body = new HashMap<>();
        Response gameResponse = apiClient.getGames();
        assertEquals(gameResponse.getStatusCode(), 200);
        GameResponseModel gameResponseModel = gameResponse.as(GameResponseModel.class);
        assertFalse(gameResponseModel.getGames().isEmpty());
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, List<Map<String, Object>>> body1 = new HashMap<>();
        List<Map<String, Object>> body2 = new ArrayList<>();
        for(int k = 0; k < 3; k++) {
            for (int i = 1; i < 2; i++) {
                body = Map.of("item_uuid", gameResponseModel.getGames().get(k).getUuid(), "quantity", (k+1)*2);
                body2.add(body);

            }
            body1.put("items", body2);
            Response orderResponse = apiClient.addOrders(userId, body1);
            assertEquals(orderResponse.getStatusCode(), 200);
            OrderModel orderModel = orderResponse.as(OrderModel.class);
            SharedOrders.sharedOrders.add(orderModel);
        }



    }

    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List all the Orders of the Users")
    @Order(2)
    public void ListAllOrders() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response orderResponse = apiClient.getOrders(userId);
        assertEquals(orderResponse.getStatusCode(), 200);
        OrdersResponseModel orderModel = orderResponse.as(OrdersResponseModel.class);
        assertFalse(orderModel.getOrders().isEmpty());
        assertEquals(3,orderModel.getMeta().getTotal());
        assertTrue(orderModel.getOrders().stream().allMatch(order -> order.getStatus().equals("open")));

    }


    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List all the Orders of the User with Limit")
    @Order(3)
    public void ListAllOrdersWithLimit() {
        Map<String, Object> body = new HashMap<>();
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        body.put("limit", 1);
        Response orderResponse = apiClient.getOrders(userId, body);
        assertEquals(orderResponse.getStatusCode(), 200);
        OrdersResponseModel orderModel = orderResponse.as(OrdersResponseModel.class);
        assertFalse(orderModel.getOrders().isEmpty());
        assertEquals(3,orderModel.getMeta().getTotal());
        assertTrue(orderModel.getOrders().stream().allMatch(order -> order.getStatus().equals("open")));
        assertEquals(1,orderModel.getOrders().size());

    }

    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add the same order at one time")
    @Order(4)
    public void AddSameOrder() {
        Map<String, Object> body = new HashMap<>();
        Response gameResponse = apiClient.getGames();
        assertEquals(gameResponse.getStatusCode(), 200);
        GameResponseModel gameResponseModel = gameResponse.as(GameResponseModel.class);
        assertFalse(gameResponseModel.getGames().isEmpty());
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, List<Map<String, Object>>> body1 = new HashMap<>();
        List<Map<String, Object>> body2 = new ArrayList<>();
        for(int k = 0; k < 1; k++) {
            for (int i = 1; i < 3; i++) {
                body = Map.of("item_uuid", gameResponseModel.getGames().get(0).getUuid(), "quantity", (k+1)*2);
                body2.add(body);

            }
            body1.put("items", body2);
            Response orderResponse = apiClient.addOrders(userId, body1);
            assertEquals(400, orderResponse.getStatusCode());
            ErrorResponseModel orderModel = orderResponse.as(ErrorResponseModel.class);
            assertTrue(orderModel.getMessage().contains("Items with the following \"uuid\" are duplicated:"));

        }

    }

    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update the Order status to Canceled")
    @Order(5)
    public void UpdateOrderStatus() {
        Map<String, Object> body = new HashMap<>();
        String orderId = SharedOrders.sharedOrders.get(0).getUuid();
        body.put("status", "canceled");
        Response orderResponse = apiClient.updateOrdersStatus(orderId, body);
        assertEquals(orderResponse.getStatusCode(), 200);
        OrderModel orderModel = orderResponse.as(OrderModel.class);
        assertEquals("canceled", orderModel.getStatus());

    }

    @Test
    @Description("Validate All Orders APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update the Order Status Again after it is updated")
    @Order(6)
    public void UpdateOrderStatusAgain() {
        Map<String, Object> body = new HashMap<>();
        String orderId = SharedOrders.sharedOrders.get(0).getUuid();
        body.put("status", "canceled");
        Response orderResponse = apiClient.updateOrdersStatus(orderId, body);
        assertEquals(422, orderResponse.getStatusCode());
        ErrorResponseModel orderModel = orderResponse.as(ErrorResponseModel.class);
        assertEquals("Operation forbidden. Order \"status\": the following statuses cannot be changed: completed, canceled", orderModel.getMessage());

    }
}
