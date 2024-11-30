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
import org.junit.jupiter.api.Test;
import shared.SharedUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginUserTests extends TestBase {

    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Story("Invalid Login Credentials")
    @Description("Test to verify invalid login credentials")
    @Severity(SeverityLevel.CRITICAL)
    public void loginUserWithInvalidCredentials() {
        LoginModel loginModel = LoginModel.createLoginUser("invalidUser@test.com", "invalidPassword");
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
    public void loginUserWithCorrectEmailAddress() {
        LoginModel loginModel = LoginModel.createLoginUser(SharedUser.existingUser.getEmail(), "invalidPassword");
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
    public void loginUser() {

        LoginModel loginModel = LoginModel.createLoginUser(SharedUser.sharedUsers.get(0).getEmail(), SharedUser.sharedUsers.get(0).getPassword());
        Response response = apiClient.loginUser(loginModel);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 404) {
            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Received Response Status: " + response.getStatusCode());

            UserModel userModel = response.as(UserModel.class);

            assertTrue(SharedUser.sharedUsers.stream().anyMatch(user -> user.getEmail().equals(userModel.getEmail())));
            AllureManager.addStep("Verify User Data", Status.PASSED,
                    "Received User Data: " + userModel);

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

}
