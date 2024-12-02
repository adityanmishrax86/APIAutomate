package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.generic.ErrorResponseModel;
import framework.models.users.LoginModel;
import framework.models.users.UserModel;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginUserTests extends TestBase {

    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Story("Invalid Login Credentials")
    @Description("Test to verify invalid login credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Order(1)
    public void loginUserWithInvalidCredentials() {
        LoginModel loginModel = LoginModel.builder().email("invalidUser@test.com").password("invalidPassword").build();
        Response response = apiClient.loginUser(loginModel);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 404) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "Could not find user with given credentials";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Invalid Login Credentials")
    @Description("Test to verify invalid login credentials with correct email address")
    @Severity(SeverityLevel.CRITICAL)
    @Order(2)
    public void loginUserWithCorrectEmailAddress() {
        LoginModel loginModel = LoginModel.builder().email(SharedUser.existingUser.getEmail()).password("invalidPassword").build();
        Response response = apiClient.loginUser(loginModel);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 404) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "Could not find user with given credentials";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Story("Invalid Login Credentials")
    @Description("Test to verify invalid login credentials with correct email address")
    @Severity(SeverityLevel.CRITICAL)
    @Order(3)
    public void loginUser() {

        LoginModel loginModel = LoginModel.builder()
                .email(SharedUser.sharedUsers.get(0).getEmail())
                .password(SharedUser.sharedUsers.get(0).getPassword())
                .build();
        Response response = apiClient.loginUser(loginModel);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 404) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserModel userModel = response.as(UserModel.class);

            assertTrue(SharedUser.sharedCreatedUsers.stream().anyMatch(user -> user.getEmail().equals(userModel.getEmail())));
            AllureManager.addStep("Verify User Data", Status.PASSED,
                    "Received User Data: " + userModel);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

}
