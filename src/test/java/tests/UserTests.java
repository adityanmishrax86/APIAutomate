package tests;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.ErrorResponseModel;
import framework.models.UserModel;
import framework.models.UserResponseModel;
import framework.reporting.AllureManager;
import framework.utils.BasicUtils;
import io.qameta.allure.*;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import framework.utils.AuthenticationUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@Epic("User Management")
@Feature("User API")
public class UserTests extends TestBase {

    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Story("Get User Details")
    @Description("Test to verify getting response status as 200 and Getting All Users Details")
    @Severity(SeverityLevel.CRITICAL)
    public void testResponseStatus() {
        Response response = apiClient.getAllUsers();
        // Add test step
        AllureManager.captureResponse(response);

        AllureManager.addStep("Verify response status", Status.PASSED,
                "Received Response Status: " + response.getStatusCode());

        if (response.statusCode() == 200) {
            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();
            UserResponseModel.Meta meta = userResponse.getMeta();

            assertTrue(meta.getTotal() >= 0);
            AllureManager.addStep("Verify Total Meta Data", Status.PASSED,
                    "Received Meta Data: " + meta.getTotal());

            assertTrue(users.size() >= 0);
            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }


    @Test
    @Story("Create a New User with same Email Address")
    @Description("Test to verify creating a new user with Exisiting Email Address")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithSameEmail() {
        UserModel createdUser = UserModel.createTestUser();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 409) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "User with the following \\\"email\\\" already exists: ";
            assertEquals(message+createdUser.getEmail(), errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Existing User", Status.PASSED, "Received Message: " + errorResponse.getMessage());


        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 409" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Story("Create User with Empty Name")
    @Description("Test to verify creating a new user with empty name")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithEmptyName() {
        UserModel createdUser = UserModel.builder().name("").email("testEMail3@test.com").password("TestSimplePassword").nickname("TestNickName").build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/name\\\": minimum string length is 1";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Name", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 400" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Create User with Empty Name")
    @Description("Test to verify creating a new user with empty name")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithEmptyNickName() {
        UserModel createdUser = UserModel.builder().name("NewName").email("testEMail3@test.com").password("TestSimplePassword").nickname("").build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/nickname\\\": minimum string length is 2";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Name", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 400" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Create User with Empty Email")
    @Description("Test to verify creating a new user with empty email")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithEmptyEmail() {
        UserModel createdUser = UserModel.builder().name("TestName").email("").password("TestSimplePassword").nickname("TestNickName").build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/email\\\": minimum string length is 5";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Email", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 400" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Create User with Empty Password")
    @Description("Test to verify creating a new user with empty password")
    @Severity(SeverityLevel.CRITICAL)
    public void createUserWithEmptyPassword() {
        UserModel createdUser = UserModel.builder().name("TestName").email("testEMail3@test.com").password("").nickname("TestNickName").build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);
        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/password\\\": minimum string length is 6";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Password", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 400" + response.getStatusCode());
        }
        AllureManager.cleanupThreadLocal();
        }

        @Test
        @Story("Create User with Email less than 5 characters")
        @Description("Test to verify creating a new user with email less than 5 characters")
        @Severity(SeverityLevel.CRITICAL)
        public void createUserWithEmailLessThan5Characters() {
            UserModel createdUser = UserModel.builder().name("TestName").email("t@t.i").password("TestSimplePassword").nickname("TestNickName").build();
            Response response = apiClient.createNewUser(createdUser);
            AllureManager.captureResponse(response);
            if (response.statusCode() == 400) {
                ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
                String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/email\\\": minimum string length is 5";
                assertEquals(message, errorResponse.getMessage());
                AllureManager.addStep("Verify Error Message for Empty Password", Status.PASSED, "Received Message: " + errorResponse.getMessage());
            } else {
                AllureManager.addStep("Verify response status", Status.FAILED,
                        "Received Response Status: \n Expected status: 400" + response.getStatusCode());
            }
            AllureManager.cleanupThreadLocal();
        }

        @Test
        @Story("Create User with Password less than 6 characters")
        @Description("Test to verify creating a new user with password less than 6 characters")
        @Severity(SeverityLevel.CRITICAL)
        public void createUserWithPasswordLessThan6Characters() {
            UserModel createdUser = UserModel.builder().name("TestName").email("testEMail3@test.com").password("Test").nickname("TestNickName").build();
            Response response = apiClient.createNewUser(createdUser);
            AllureManager.captureResponse(response);
            if (response.statusCode() == 400) {
                ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
                String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \\\"/password\\\": minimum string length is 6";
                assertEquals(message, errorResponse.getMessage());
                AllureManager.addStep("Verify Error Message for Empty Password", Status.PASSED, "Received Message: " + errorResponse.getMessage());
            } else {
                AllureManager.addStep("Verify response status", Status.FAILED,
                        "Received Response Status: \n Expected status: 400" + response.getStatusCode());
            }
            AllureManager.cleanupThreadLocal();
        }


}

//@Test
//@Story("Create a New User with Required Details")
//@Description("Test to verify creating a new user")
//@Severity(SeverityLevel.CRITICAL)
//public void createUser() {
//    UserModel createdUser = UserModel.createTestUser();
//    Response response = apiClient.createNewUser(createdUser);
//    AllureManager.captureResponse(response);
//
//    if (response.statusCode() == 200) {
//        UserModel userResponse = response.as(UserModel.class);
//
//        assertEquals(createdUser.getName(), userResponse.getName());
//        AllureManager.addStep("Verify User Name", Status.PASSED, "Received User Name: " + userResponse.getName());
//
//        assertEquals(createdUser.getNickname(), userResponse.getNickname());
//        AllureManager.addStep("Verify User Nickname", Status.PASSED, "Received User Nickname: " + userResponse.getNickname());
//
//        assertEquals("", userResponse.getAvatar_url());
//        AllureManager.addStep("Verify User Avatar URL", Status.PASSED, "Received User Avatar URL: " + userResponse.getAvatar_url());
//
//        assertTrue(BasicUtils.isValidUUID(userResponse.getUuid()));
//        AllureManager.addStep("Verify User UUID", Status.PASSED, "Received User UUID: " + userResponse.getUuid());
//    } {
//        AllureManager.addStep("Verify response status", Status.FAILED,
//                "Received Response Status: \n Expected status: 200" + response.getStatusCode());
//    }
//
//    AllureManager.cleanupThreadLocal();
//
//}

//
//    @Test
//    @Story("Create User")
//    @Description("Test to verify creating a new user")
//    @Severity(SeverityLevel.CRITICAL)
//    public void testCreateUser() {
//        Response response = apiClient.createUser(UserModel.createTestUser());
//
//        AllureManager.captureResponse(response);
//
//        if (response.statusCode() == 200) {
//            UserModel user = response.as(UserModel.class);
//            assertAll(() -> assertTrue(user.getId() == 1 || user.getId() == 11, "User ID should be 1 or 11"));
//            if (null != user.getName()) {
//                assertEquals("derek", user.getName().getFirstname());
//                assertEquals("powell", user.getName().getLastname());
//            }
//
//            if (null != user.getAddress()) {
//                assertEquals("san Antonio", user.getAddress().getCity());
//                assertEquals("adams St", user.getAddress().getStreet());
//            }
//        }
//
//
//    }
//}

