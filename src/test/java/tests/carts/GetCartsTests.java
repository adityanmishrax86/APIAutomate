package tests.carts;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.carts.CartItemModel;
import framework.models.carts.CartModel;
import framework.models.games.GameResponseModel;
import framework.models.generic.ErrorResponseModel;
import framework.utils.AuthenticationUtils;
import framework.utils.BasicUtils;
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
import shared.SharedUser;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetCartsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Items to Cart")
    @Order(1)
    public void AddGamesToCart() {
        Map<String, Object> body = new HashMap<>();
        Response gameResponse = apiClient.getGames();
        assertEquals(200, gameResponse.getStatusCode());
        GameResponseModel gameResponseModel = gameResponse.as(GameResponseModel.class);
        assertFalse(gameResponseModel.getGames().isEmpty());
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();

        for (int i = 1; i < 3; i++) {
           body = Map.of("item_uuid", gameResponseModel.getGames().get(i-1).getUuid(), "quantity", 1);
            Response cartResponse = apiClient.addGameToCart(userId, body);

            assertEquals(cartResponse.getStatusCode(), 200);
            SharedCart.sharedCarts.add(cartResponse.as(CartModel.class));
        }

    }

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Clear Cart items")
    @Order(2)
    public void ClearCartItems() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, Object> body = Map.of("item_uuid", BasicUtils.generateUUID());
        Response cartResponse = apiClient.clearCartItems(userId, body);
        assertEquals(200,cartResponse.getStatusCode());
        CartModel cartModel = cartResponse.as(CartModel.class);
        assertTrue(cartModel.getItems().isEmpty());
        assertEquals(0, cartModel.getTotal_price());
    }

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Items to Cart")
    @Order(3)
    public void AddGamesToCartAgain() {
        Map<String, Object> body = new HashMap<>();
        Response gameResponse = apiClient.getGames();
        assertEquals(200,gameResponse.getStatusCode());
        GameResponseModel gameResponseModel = gameResponse.as(GameResponseModel.class);
        assertFalse(gameResponseModel.getGames().isEmpty());
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();

        for (int i = 1; i < 3; i++) {
            body = Map.of("item_uuid", gameResponseModel.getGames().get(i-1).getUuid(), "quantity", 1);
            Response cartResponse = apiClient.addGameToCart(userId, body);

            assertEquals(200,cartResponse.getStatusCode());
            SharedCart.sharedCarts.add(cartResponse.as(CartModel.class));
        }

    }


    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get All cart items")
    @Order(4)
    public void GetCartItems() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response cartResponse = apiClient.getUserCart(userId);
        assertEquals(200,cartResponse.getStatusCode());
        CartModel cartModel = cartResponse.as(CartModel.class);
        assertFalse(cartModel.getItems().isEmpty());
        assertEquals(cartModel.getTotal_price(), (int) cartModel.getItems().stream().map(x -> x.getTotal_price() * x.getQuantity()).reduce(0, Integer::sum));
    }

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Change Cart items")
    @Order(5)
    public void ChangeCartItems() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, Object> body = Map.of("item_uuid", SharedCart.sharedCarts.get(0).getItems().get(0).getItem_uuid(), "quantity", 5);
        Response cartResponse = apiClient.changeCartItems(userId, body);
        assertEquals(200, cartResponse.getStatusCode());
        CartModel cartModel = cartResponse.as(CartModel.class);
        assertFalse(cartModel.getItems().isEmpty());
        assertEquals(cartModel.getTotal_price(), (int) cartModel.getItems().stream().map(CartItemModel::getTotal_price).reduce(0, Integer::sum));

    }

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove Cart items")
    @Order(6)
    public void RemoveCartItems() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, Object> body = Map.of("item_uuid", SharedCart.sharedCarts.get(0).getItems().get(0).getItem_uuid());
        Response cartResponse = apiClient.removeCartItems(userId, body);
        assertEquals(200,cartResponse.getStatusCode());
        CartModel cartModel = cartResponse.as(CartModel.class);
        assertFalse(cartModel.getItems().isEmpty());
        assertEquals(cartModel.getTotal_price(), (int) cartModel.getItems().stream().map(CartItemModel::getTotal_price).reduce(0, Integer::sum));

    }

    @Test
    @Description("Validate All Cart APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove cart items with invalid id")
    @Order(7)
    public void RemoveCartItemsWithInvalidUUID() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, Object> body = Map.of("item_uuid", BasicUtils.generateUUID());
        Response cartResponse = apiClient.removeCartItems(userId, body);
        assertEquals(404,cartResponse.getStatusCode());
        ErrorResponseModel errorResponseModel = cartResponse.as(ErrorResponseModel.class);
        assertEquals(("Could not find cart_item with \"uuid\": " + body.get("item_uuid")), errorResponseModel.getMessage());

    }





}
