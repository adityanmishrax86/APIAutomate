package framework.utils;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import framework.models.UserModel;

import static io.restassured.RestAssured.given;

public class APIUtils {
    @Step("Making GET request to {endpoint}")
    public static Response get(String endpoint, String token) {
        return given()
//                .header("Authorization", "Bearer " + token)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making POST request to {endpoint}")
    public static Response post(String endpoint, Object body, String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }
}
