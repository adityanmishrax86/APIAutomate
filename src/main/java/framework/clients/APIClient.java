package framework.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import framework.utils.APIUtils;

public class APIClient {
    private final String token;

    public APIClient(String token) {
        this.token = token;
    }

    @Step("Get user by ID: {userId}")
    public Response getUserById(String userId) {
        return APIUtils.get("/users/" + userId, token);
    }

    @Step("Create new user")
    public Response createUser(Object userBody) {
        return APIUtils.post("/users", userBody, token);
    }
}
