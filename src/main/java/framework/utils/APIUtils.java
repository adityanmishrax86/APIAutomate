package framework.utils;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.io.File;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class APIUtils {
    @Step("Making GET request to {endpoint}")
    public static Response get(String endpoint, String token, String taskId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .header("X-Task-Id",taskId)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making GET request to {endpoint}")
    public static Response getWOAuth(String endpoint, String taskId) {
        return given()
                .contentType(ContentType.JSON)
                .header("X-Task-Id",taskId)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making GET request to {endpoint}")
    public static Response getWithQueryParams(String endpoint, String token, Map<String, Object> queryParams, String taskId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .header("X-Task-Id",taskId)
                .params(queryParams)
                .when()
                .get(endpoint)
                .then()
                .extract()
                .response();
    }


    @Step("Making POST request to {endpoint}")
    public static Response post(String endpoint, Object body, String token, String taskId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .header("X-Task-Id",taskId)
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making PUT request to {endpoint}")
    public static Response put(String endpoint, Object body, String token, String taskId) {
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .header("X-Task-id",taskId)
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .extract()
                .response();
    }


    @Step("Making DELETE request to {endpoint}")
    public static Response delete(String endpoint, String token, String taskId) {
        return given()
                .header("Authorization", "Bearer " + token)
                .header("X-Task-id",taskId)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making DELETE request to {endpoint}")
    public static Response deleteWOAuth(String endpoint, String taskId) {
        return given()
                .header("X-Task-id",taskId)
                .when()
                .delete(endpoint)
                .then()
                .extract()
                .response();
    }

    @Step("Making PUT request with File Upload to {endpoint}")
    public static Response putWithFile(String endpoint, File file, String token, String taskId) {
        return given()
                .multiPart("avatar_file", file)
                .contentType(ContentType.MULTIPART)
                .header("Authorization", "Bearer " + token)
                .header("X-Task-id",taskId)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }
}
