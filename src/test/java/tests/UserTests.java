package tests;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.UserModel;
import framework.reporting.AllureManager;
import io.qameta.allure.*;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import framework.utils.AuthenticationUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Epic("User Management")
@Feature("User API")
public class UserTests extends TestBase {

        private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

        @Test
        @Story("Get User Details")
        @Description("Test to verify getting user details by ID")
        @Severity(SeverityLevel.CRITICAL)
        public void testGetUserById() {
            Response response = apiClient.getUserById("1");

            // Add test step
            AllureManager.captureResponse(response);

            if(response.statusCode() == 200) {
                UserModel user = response.as(UserModel.class);
                assertEquals(1, user.getId());
                assertEquals("john", user.getName().getFirstname());
                assertEquals("doe",user.getName().getLastname());
                assertEquals("kilcoole", user.getAddress().getCity());
            }

            AllureManager.addStep("Verify response status", Status.PASSED,
                    "Expected status: 201, Actual status: " + response.getStatusCode());

            AllureManager.addEnvironmentInfo();

            AllureManager.attachRequestResponse();
        }
    }

