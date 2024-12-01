package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.generic.ErrorResponseModel;
import framework.reporting.AllureManager;
import framework.utils.AuthenticationUtils;
import framework.utils.BasicUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import shared.SharedUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeleteUsersTests extends TestBase {

    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());


    @Test
    @Story("Delete a user")
    @Description("Delete a user")
    @Severity(SeverityLevel.CRITICAL)
    @Order(1)
    public void deleteUser() {

        String uuid = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response response = apiClient.deleteUsers(uuid);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 204) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            setAllUsers();

            assertTrue(SharedUser.sharedCreatedUsers.stream().noneMatch(user -> user.getUuid().equals(uuid)));
            AllureManager.addStep("Verify User Data", Status.PASSED,
                    "Received User Data: " + uuid);

        } else if (response.statusCode() == 500) {
            AllureManager.addStep("Verify response status", Status.FAILED,"Unable to receive response for Unknown UUID.");
            assert false;
        }
        else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Story("Delete an unknown user")
    @Description("Delete an unknown user")
    @Severity(SeverityLevel.CRITICAL)
    @Order(2)
    public void deleteUnknownUser() {

        String uuid = BasicUtils.generateUUID();
        Response response = apiClient.deleteUsers(uuid);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 404) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "Could not find user with \"uuid\": " + uuid;
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else if (response.statusCode() == 500) {
            AllureManager.addStep("Verify response status", Status.FAILED,"Unable to receive response for Unknown UUID.");
            assert false;
        }
        else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Description("Delete a user with an invalid UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Delete User API")
    @Order(3)
    public void testDeleteUserWithInvalidUUID() {
        String invalidUUID = "123-abc";
        Response response = apiClient.deleteUsers(invalidUUID);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("parameter \"user_uuid\" in path has an error: minimum string length is 36", errorResponse.getMessage());
    }

    @Test
    @Order(4)
    @Description("Delete a user without authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Delete User API")
    public void testDeleteUserWithoutAuthentication() {
        String userUUID = "valid-user-uuid";
        Response response = apiClient.deleteUsersWOAuth(userUUID);

        assertEquals(401, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("security requirements failed: authentication failed, please set correct \"Bearer\" header", errorResponse.getMessage());
    }

    @Test
    @Description("Test SQL injection vulnerability")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Delete User API")
    @Order(5)
    public void testDeleteUserSQLInjection() {
        String sqlInjectionPayload = "1'; DROP TABLE users;--";
        Response response = apiClient.deleteUsers(sqlInjectionPayload);
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);

        assertEquals(400, response.getStatusCode());
        assertTrue(errorResponse.getMessage().equals("parameter \"user_uuid\" in path has an error: minimum string length is 36"));
    }
}
