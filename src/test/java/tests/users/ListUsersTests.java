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
    @Order(2)
    public void listAllUsersWithPaginationWithDigit() {
        Map<String, Object> limits = Map.of("limit", 2);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            assertTrue(users.size()  == 2);
            assertTrue(userResponse.getMeta().getTotal() > users.size());
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
    @Order(3)
    public void listAllUsersWithOffset() {
        Map<String, Object> limits = Map.of("offset", 3);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 200) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            assertTrue(userResponse.getMeta().getTotal() > users.size());

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
    @Order(4)
    public void listAllUsersWithBothOffsetAndLimit() {
        Map<String, Object> limits = Map.of("offset", 2, "limit", 2);
        Response response = apiClient.getAllUsersWithParams(limits);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();

            List<UserModel> offsetSharedUsers = SharedUser.sharedCreatedUsers.subList(2, SharedUser.sharedCreatedUsers.size());
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
