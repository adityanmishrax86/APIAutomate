package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.generic.ErrorResponseModel;
import framework.models.users.UserModel;
import framework.models.users.UserResponseModel;
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

import static org.junit.jupiter.api.Assertions.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetUserTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());


    @Test
    @Order(1)
    @Description("Fetch a user with a valid UUID")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Fetch User API")
    public void testFetchUserWithValidUUID() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid(); // Replace with a real UUID
        Response response = apiClient.getUser(userUUID);

        assertEquals(200, response.getStatusCode());
        UserModel user = response.as(UserModel.class);
        assertNotNull(user);
        assertEquals(userUUID, user.getUuid());
        assertNotNull(user.getEmail());
        assertNotNull(user.getName());
        assertTrue(user.getUuid().equals(userUUID));
        assertTrue(user.getEmail().equals(SharedUser.sharedCreatedUsers.get(0).getEmail()));
        assertTrue(user.getName().equals(SharedUser.sharedCreatedUsers.get(0).getName()));
        assertTrue(user.getNickname().equals(SharedUser.sharedCreatedUsers.get(0).getNickname()));
        assertEquals("", user.getAvatar_url());
    }

    @Test
    @Order(2)
    @Description("Fetch a user with an invalid UUID format")
    @Severity(SeverityLevel.NORMAL)
    @Story("Fetch User API")
    public void testFetchUserWithInvalidUUID() {
        String invalidUUID = "123-abc";
        Response response = apiClient.getUser(invalidUUID);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("parameter \"user_uuid\" in path has an error: minimum string length is 36", errorResponse.getMessage());
    }

    @Test
    @Order(3)
    @Description("Fetch a user with a non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Fetch User API")
    public void testFetchUserWithNonExistingUUID() {
        String nonExistingUUID = "11111111-1111-1111-1111-111111111111";
        Response response = apiClient.getUser(nonExistingUUID);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("parameter \"user_uuid\" in path has an error: string doesn't match the regular expression \"[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-4[a-fA-F0-9]{3}-[89abAB][a-fA-F0-9]{3}-[a-fA-F0-9]{12}\"", errorResponse.getMessage());
    }

    @Test
    @Order(4)
    @Description("Fetch a user with a non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Fetch User API")
    public void testFetchUserWithNonExistingUUID2() {
        String nonExistingUUID = BasicUtils.generateUUID();
        Response response = apiClient.getUser(nonExistingUUID);

        assertEquals(404, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("Could not find user with \"uuid\": " + nonExistingUUID, errorResponse.getMessage());
    }

    @Test
    @Order(5)
    @Description("Fetch a user without authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Fetch User API")
    public void testFetchUserWithoutAuthentication() {
        String userUUID = "00000000-0000-4562-b3fc-2c963f66afa6";
        Response response = apiClient.getUserWOAuth(userUUID);

        assertEquals(401, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("security requirements failed: authentication failed, please set correct \"Bearer\" header", errorResponse.getMessage());
    }


    @Test
    @Order(6)
    @Description("SQL Injection Test")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Fetch User API")
    public void testSQLInjection() {
        String sqlInjectionPayload = "1' OR '1'='1";
        Response response = apiClient.getUser(sqlInjectionPayload);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("parameter \"user_uuid\" in path has an error: minimum string length is 36", errorResponse.getMessage());
    }

}
