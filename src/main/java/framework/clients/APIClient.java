package framework.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import framework.utils.APIUtils;

import java.io.File;
import java.util.Map;

public class APIClient {
    private final String token;

    public APIClient(String token) {
        this.token = token;
    }

    @Step("Get All Users")
    public Response getAllUsers() {
        return APIUtils.get("users/", token, "api-21");
    }

    @Step("Create a New User")
    public Response createNewUser(Object body) {
        return APIUtils.post("users/", body, token, "api-3");
    }

    @Step("Get All users with Params")
    public Response getAllUsersWithParams(Map<String, Object> params) {
        return APIUtils.getWithQueryParams("users/", token, params, "api-6");
    }

    public Response deleteUsers(String uuid) {
        return APIUtils.delete("users/" + uuid, token, "api-1");
    }

    public Response deleteUsersWOAuth(String uuid) {
        return APIUtils.deleteWOAuth("users/" + uuid, "api-1");
    }

    public Response loginUser(Object loginBody) {
        return APIUtils.post("users/login", loginBody, token, "api-7");
    }

    @Step("Get an User")
    public Response getUser(String uuid) {
        return APIUtils.get("users/" + uuid, token, "api-23");
    }

    @Step("Get Current Without Auth User")
    public Response getUserWOAuth(String uuid) {
        return APIUtils.getWOAuth("users/" + uuid, "api-23");
    }

    @Step("Update user")
    public Response updateUser(String uuid, Object body) {
        return APIUtils.put("users/" + uuid, body, token, "api-4");
    }

    @Step("Get Games")
    public Response getGames() {
        return APIUtils.get("games/", token, "api-2");
    }

    @Step("Get Game by ID: {gameId}")
    public Response getGameById(String gameId) {
        return APIUtils.get("games/" + gameId, token, "api-9");
    }

    @Step("Search a Game by Name: {gameName}")
    public Response searchGameByName(Map<String, Object> params) {
        return APIUtils.getWithQueryParams("games/search/", token, params, "api-2");
    }

    @Step("Get Categories")
    public Response getCategories() {
        return APIUtils.get("categories/", token, "api-10");
    }

    @Step("Get Category by ID: {categoryId}")
    public Response getGamesAsPerCategory(String categoryId) {
        return APIUtils.get("categories/" + categoryId + "/games", token, "api-10");
    }

    @Step("Get User wishlist")
    public Response getUserWishlist(String userId) {
        return APIUtils.get("users/" + userId + "/wishlist", token, "api-5");
    }

    @Step("Add Game to User wishlist")
    public Response addGameToWishlist(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/wishlist/add", body, token, "api-5");
    }

    @Step("Add Game to User wishlist")
    public Response addGameToWishlist2(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/wishlist/add", body, token, "api-25");
    }

    @Step("Add Game to User wishlist")
    public Response addGameToWishlist3(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/wishlist/add", body, token, "api-1");
    }

    @Step("Remove Game from User wishlist")
    public Response removeGameFromWishlist(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/wishlist/remove", body, token, "api-8");
    }

    @Step("Update User Avatar URL")
    public Response updateUserAvatar(String userId, File file) {
        return APIUtils.putWithFile("users/" + userId + "/avatar", file, token, "api-11");
    }

    @Step("Get User Cart")
    public Response getUserCart(String userId) {
        return APIUtils.get("users/" + userId + "/cart", token, "api-12");
    }

    @Step("Add Game to User Cart")
    public Response addGameToCart(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/cart/add", body, token, "api-12");
    }

    @Step("Change Cart Items")
    public Response changeCartItems(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/cart/change", body, token, "api-13");
    }

    @Step("Remove Cart Items")
    public Response removeCartItems(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/cart/remove", body, token, "api-14");
    }

    @Step("Clear Cart Items")
    public Response clearCartItems(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/cart/clear", body, token, "api-15");
    }

    @Step("Add Orders")
    public Response addOrders(String userId, Object body) {
        return APIUtils.post("users/" + userId + "/orders", body, token, "api-16");
    }

    @Step("Get Orders")
    public Response getOrders(String userId, Map<String, Object> params ) {
        return APIUtils.getWithQueryParams("users/" + userId + "/orders", token,params , "api-17");
    }

    @Step("Get Orders")
    public Response getOrders(String userId) {
        return APIUtils.get("users/" + userId + "/orders", token, "api-17");
    }

    @Step("Update Orders Status")
    public Response updateOrdersStatus(String orderId, Object body) {
        return APIUtils.put("orders/" + orderId +"/status",body, token, "api-18");
    }

    @Step("Create Payments")
    public Response createPayments(String userId, Object body) {
        return APIUtils.post("users/" + userId +"/payments",body, token, "api-18");
    }

    @Step("Get A Single Payment")
    public Response getASinglePayment(String paymentId) {
        return APIUtils.get("payments/" + paymentId , token, "api-19");
    }

}
