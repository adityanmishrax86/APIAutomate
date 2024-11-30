package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.generic.ErrorResponseModel;
import framework.models.generic.Meta;
import framework.models.users.UserModel;
import framework.models.users.UserResponseModel;
import framework.reporting.AllureManager;
import framework.utils.AuthenticationUtils;
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

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ListUsersTests extends TestBase {

    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Story("List All Users")
    @Description("Test to verify listing all users")
    @Severity(SeverityLevel.CRITICAL)
    @Order(1)
    public void listAllUsers() {

        Response response = apiClient.getAllUsers();
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();
            Meta meta = userResponse.getMeta();

            assertTrue(meta.getTotal() >= 0);
            AllureManager.addStep("Verify Total Meta Data", Status.PASSED,
                    "Received Meta Data: " + meta.getTotal());

            assertTrue(users.size() >= 0);
            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);

            assertTrue(users.size() <= 10);
            AllureManager.addStep("Verify Users by Default top 10 users", Status.PASSED, "Received Users Data: " + users);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPagination() {
        Map<String, Object> limits = Map.of("limit", "10");
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            assertTrue(users.size() <= 10);
            AllureManager.addStep("Verify only top 10 users are retrieved", Status.PASSED, "Received Users Data: " + users);
    } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithDigit() {
        Map<String, Object> limits = Map.of("limit", 10);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            assertTrue(users.size() <= 10);
            AllureManager.addStep("Verify only top 10 users are retrieved", Status.PASSED, "Received Users Data: " + users);
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithStringValue() {
        Map<String, Object> limits = Map.of("limit", "Ten");
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = String.format("parameter \"limit\" in query has an error: value %s: an invalid integer: invalid syntax",limits.get("limit"));
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithFloatValue() {
        Map<String, Object> limits = Map.of("limit", 10.0);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = String.format("parameter \"limit\" in query has an error: value %s: an invalid integer: invalid syntax",limits.get("limit"));
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithLimitMoreThan100() {
        Map<String, Object> limits = Map.of("limit", 101);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "parameter \"limit\" in query has an error: number must be at most 100";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithLimitEqualTo0() {
        Map<String, Object> limits = Map.of("limit", 0);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "parameter \"limit\" in query has an error: number must be at least 1";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithLimitNegativeValue() {
        Map<String, Object> limits = Map.of("limit", -12);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "parameter \"limit\" in query has an error: number must be at least 1";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithPaginationWithLimitEmpty() {
        Map<String, Object> limits = Map.of("limit", "");
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());
            try {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "parameter \"limit\" in query has an error: value  : an invalid integer: invalid syntax";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
            } catch (IllegalStateException e) {
                AllureManager.addStep("Received Error Response.", Status.FAILED, "Recieved Response as plain text instead of json. \n" + e.getMessage());
            }


        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffset() {
        Map<String, Object> limits = Map.of("offset", 3);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            List<UserModel> offsetSharedUsers = SharedUser.sharedUsers.subList(3, SharedUser.sharedUsers.size());
            assertEquals(offsetSharedUsers.get(0).getUuid(), users.get(0).getUuid());

            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffsetWith0() {
        Map<String, Object> limits = Map.of("offset", 0);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            assertEquals(SharedUser.sharedUsers.get(0).getUuid(), users.get(0).getUuid());

            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffsetWithNegativeValue() {
        Map<String, Object> limits = Map.of("offset", -2);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "parameter \"offset\" in query has an error: number must be at least 0";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffsetWithStringValue() {
        Map<String, Object> limits = Map.of("offset", "Ten");
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = String.format("parameter \"offset\" in query has an error: value %s: an invalid integer: invalid syntax",limits.get("offset"));
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffsetWithFloatValue() {
        Map<String, Object> limits = Map.of("offset", 10.0);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = String.format("parameter \"offset\" in query has an error: value %s: an invalid integer: invalid syntax",limits.get("offset"));
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithOffsetWithLimitEmpty() {
        Map<String, Object> limits = Map.of("offset", "");
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());
            try {
                ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
                String message = "parameter \"offset\" in query has an error: value  : an invalid integer: invalid syntax";
                assertEquals(message, errorResponse.getMessage());
                AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
            } catch (IllegalStateException e) {
                AllureManager.addStep("Received Error Response.", Status.FAILED, "Received Response as plain text instead of json. \n" + e.getMessage());
            }

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("List All Users with Pagination")
    @Description("Test to verify listing all users with pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void listAllUsersWithBothOffsetAndLimit() {
        Map<String, Object> limits = Map.of("offset", 2, "limit", 2);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            List<UserModel> offsetSharedUsers = SharedUser.sharedUsers.subList(2, SharedUser.sharedUsers.size());
            assertEquals(offsetSharedUsers.get(0).getUuid(), users.get(0).getUuid());

            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);

            assertEquals(2, users.size());
            AllureManager.addStep("Verify Users by Default top 10 users", Status.PASSED, "Received Users Data: " + users);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }


}
