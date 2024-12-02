package tests.wishlists;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.games.GameResponseModel;
import framework.models.generic.ErrorResponseModel;
import framework.models.wishlists.WishListModel;
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
import shared.SharedUser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RemoveWishlistsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Order(1)
    @Description("Validate All Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get User Wishlists")
    public void getUserWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(1).getUuid();
        Response response = apiClient.getUserWishlist(userId);

        assertEquals(200,response.getStatusCode());
        WishListModel wishListModel = response.as(WishListModel.class);
        assertTrue(wishListModel.getItems().size() >= 0);
        assertTrue(wishListModel.getUser_uuid().equals(userId));
    }

    @Test
    @Order(2)
    @Description("Validate All Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create New User Wishlist")
    public void AddUserWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response response = apiClient.getGames();
        assertEquals(200,response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertEquals(gameResponseModel.getGames().size(), 10);

        Map<String, Object> body = Map.of("item_uuid", gameResponseModel.getGames().get(0).getUuid());

        Response wishResponse = apiClient.addGameToWishlist3(userId, body);

        assertEquals(200,wishResponse.getStatusCode());
        WishListModel wishListModel = wishResponse.as(WishListModel.class);
        assertEquals(wishListModel.getItems().size(), 1);
        assertTrue(wishListModel.getItems().get(0).getUuid().equals(gameResponseModel.getGames().get(0).getUuid()));
    }

    @Test
    @Order(3)
    @Description("Validate All Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove from wishlist which is not present")
    public void RemoveUserGameNotPresentWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();

        Map<String, Object> body = Map.of("item_uuid", BasicUtils.generateUUID());

        Response wishResponse = apiClient.removeGameFromWishlist(userId, body);

        assertEquals(404,wishResponse.getStatusCode());
        ErrorResponseModel wishListModel = wishResponse.as(ErrorResponseModel.class);
        assertEquals(wishListModel.getMessage(),"Could not find game with \"uuid\": " + body.get("item_uuid"));
    }

    @Test
    @Order(4)
    @Description("Validate All Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Remove form wishlist ")
    public void RemoveUserGameWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response response = apiClient.getGames();
        assertEquals(200,response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertEquals(10,gameResponseModel.getGames().size());

        Map<String, Object> body = Map.of("item_uuid", gameResponseModel.getGames().get(0).getUuid());

        Response wishResponse = apiClient.removeGameFromWishlist(userId, body);

        assertEquals(200,wishResponse.getStatusCode());
        WishListModel wishListModel = wishResponse.as(WishListModel.class);
        assertEquals(0,wishListModel.getItems().size());
//        assertTrue(wishListModel.getItems().get(0).getUuid().equals(gameResponseModel.getGames().get(0).getUuid()));
    }

}
