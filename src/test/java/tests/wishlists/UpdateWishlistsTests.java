package tests.wishlists;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.games.GameResponseModel;
import framework.models.wishlists.WishListModel;
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
import shared.SharedUser;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateWishlistsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Order(1)
    @Description("Validate all Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get User Wishlists")
    public void getUserWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response response = apiClient.getUserWishlist(userId);

        assertEquals(response.getStatusCode(), 200);
        WishListModel wishListModel = response.as(WishListModel.class);
        assertEquals(wishListModel.getItems().size(), 0);
        assertTrue(wishListModel.getUser_uuid().equals(userId));
    }

    @Test
    @Order(2)
    @Description("Validate all Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add to User Wishlists")
    public void AddUserWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response response = apiClient.getGames();
        assertEquals(response.getStatusCode(), 200);
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertEquals(gameResponseModel.getGames().size(), 10);

        Map<String, Object> body = Map.of("item_uuid", gameResponseModel.getGames().get(0).getUuid());

        Response wishResponse = apiClient.addGameToWishlist(userId, body);

        assertEquals(wishResponse.getStatusCode(), 200);
        WishListModel wishListModel = wishResponse.as(WishListModel.class);
        assertEquals(wishListModel.getItems().size(), 1);
        assertTrue(wishListModel.getItems().get(0).getUuid().equals(gameResponseModel.getGames().get(0).getUuid()));
    }



    @Test
    @Order(3)
    @Description("Validate all Wishlist APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Add Multiple games to Wishlists")
    public void AddMultipleGamesToUserWishLists() {
        String userId = SharedUser.sharedCreatedUsers.get(1).getUuid();
        Response response = apiClient.getGames();
        assertEquals(response.getStatusCode(), 200);
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertEquals(gameResponseModel.getGames().size(), 10);

        for(int i = 1; i < gameResponseModel.getGames().size(); i++) {
            Map<String, Object> body = Map.of("item_uuid", gameResponseModel.getGames().get(i-1).getUuid());
            Response wishResponse = apiClient.addGameToWishlist2(userId, body);

            assertEquals(wishResponse.getStatusCode(), 200);
            WishListModel wishListModel = wishResponse.as(WishListModel.class);
            assertEquals(wishListModel.getItems().size(), i);
            assertTrue(gameResponseModel.getGames().contains(wishListModel.getItems().get(i-1)));
        }

    }



}

