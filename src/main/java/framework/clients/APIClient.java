package framework.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import framework.utils.APIUtils;

import java.util.Map;

public class APIClient {
    private final String token;

    public APIClient(String token) {
        this.token = token;
    }

    @Step("Get All Users")
    public Response getAllUsers() {
        return APIUtils.get("users/",  token, "api-6");
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
        return APIUtils.delete("users/"+uuid, token, "api-1");
    }

    public Response loginUser(Object loginBody) {
        return APIUtils.post("users/login", loginBody, token, "api-7");
    }

//    @Step("Get user by ID: {userId}")
//    public Response getUserById(String userId) {
//        return APIUtils.get("/users/" + userId, token);
//    }
//
//    @Step("Create new user")
//    public Response createUser(Object userBody) {
//        return APIUtils.post("/users", userBody, token);
//    }
//
//    @Step("Login user")
//    public static Response loginUser(Object loginBody) {
//        return APIUtils.post("/auth/login", loginBody);
//    }
//
//    @Step("Get Current Auth User")
//    public static Response getCurrentAuthUser(String token) {
//        return APIUtils.get("/auth/me", token);
//    }
//
//    @Step("Get All the Products")
//    public static Response getAllTheProducts(String token) {
//        return APIUtils.get("/products", token);
//    }
//
//    @Step("Get Product by ID: {productId}")
//    public static Response getProductById(String productId, String token) {
//        return APIUtils.get("/products/" + productId, token);
//    }
//
//    @Step("Get Sorted Products")
//    public static Response getSortedProducts(String token, String sortBy, String order) {
//        return APIUtils.get(String.format("/products?sortBy=%s&order=%s",sortBy,order), token);
//    }
}
