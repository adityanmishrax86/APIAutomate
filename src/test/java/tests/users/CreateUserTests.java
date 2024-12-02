package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.generic.ErrorResponseModel;
import framework.models.generic.Meta;
import framework.models.users.UserModel;
import framework.models.users.UserResponseModel;
import framework.reporting.AllureManager;
import framework.utils.AuthenticationUtils;
import framework.utils.BasicUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.model.Status;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static shared.SharedUser.existingUser;
import static shared.SharedUser.sharedUser;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CreateUserTests extends TestBase {

    private static final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
    private static final String mailDomain = "@test.com";


    @Test
    @Order(1)
    @Story("Create a New User with Required Details")
    @Description("Test to verify creating a new user")
    @Severity(SeverityLevel.CRITICAL)
    public void createUser() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(10) + mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        assertTrue(response.statusCode() == 200);
        UserModel userResponse = response.as(UserModel.class);
        sharedUser = userResponse;

        assertEquals(createdUser.getName(), userResponse.getName());
        AllureManager.addStep("Verify User Name", Status.PASSED, "Received User Name: " + userResponse.getName());

        assertEquals(createdUser.getNickname(), userResponse.getNickname());
        AllureManager.addStep("Verify User Nickname", Status.PASSED, "Received User Nickname: " + userResponse.getNickname());

        assertEquals("", userResponse.getAvatar_url());
        AllureManager.addStep("Verify User Avatar URL", Status.PASSED, "Received User Avatar URL: " + userResponse.getAvatar_url());

        assertTrue(BasicUtils.isValidUUID(userResponse.getUuid()));
        AllureManager.addStep("Verify User UUID", Status.PASSED, "Received User UUID: " + userResponse.getUuid());


        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Order(2)
    @Story("Get User Details after Created")
    @Description("Test to verify getting User Details after it is created")
    @Severity(SeverityLevel.CRITICAL)
    public void validateUserCreatedOrNot() {
        Response response = apiClient.getAllUsers();
        // Add test step
        AllureManager.captureResponse(response);

        AllureManager.addStep("Verify response status", Status.PASSED,
                "Received Response Status: " + response.getStatusCode());

        assertEquals(200, response.statusCode());
        UserResponseModel userResponse = response.as(UserResponseModel.class);
        List<UserModel> users = userResponse.getUsers();
        Meta meta = userResponse.getMeta();

        assertTrue(meta.getTotal() >= 0);
        AllureManager.addStep("Verify Total Meta Data", Status.PASSED,
                "Received Meta Data: " + meta.getTotal());

        assertTrue(users.size() > 0);
        AllureManager.addStep("Verify Users Data", Status.PASSED,
                "Received Users Data: " + users);

        if (null != sharedUser) {
            assertTrue(users.stream().anyMatch(user -> user.getEmail().equals(sharedUser.getEmail())));
            AllureManager.addStep("Verify User Email as created", Status.PASSED,
                    "Received User Email: " + sharedUser.getEmail());

            assertTrue(users.stream().anyMatch(user -> user.getNickname().equals(sharedUser.getNickname())));
            AllureManager.addStep("Verify User Nickname as created", Status.PASSED,
                    "Received User Nickname: " + sharedUser.getNickname());

            assertTrue(users.stream().anyMatch(user -> user.getName().equals(sharedUser.getName())));
            AllureManager.addStep("Verify User Name as created", Status.PASSED,
                    "Received User Name: " + sharedUser.getName());
        }


        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Create a New User with same Email Address")
    @Description("Test to verify creating a new user with Existing Email Address")
    @Severity(SeverityLevel.CRITICAL)
    @Order(3)
    public void createUserWithSameEmail() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(existingUser.getEmail())
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        assertEquals(409, response.statusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        String message = "User with the following \"email\" already exists: ";
        assertEquals(message + createdUser.getEmail(), errorResponse.getMessage());
        AllureManager.addStep("Verify Error Message for Existing User", Status.PASSED, "Received Message: " + errorResponse.getMessage());


        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Story("Create a New User with same Nick Name")
    @Description("Test to verify creating a new user with Existing Nick Name")
    @Severity(SeverityLevel.CRITICAL)
    @Order(4)
    public void createUserWithSameNickName() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(7) + mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(existingUser.getNickname())
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        assertEquals(409, response.statusCode());

        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        String message = "User with the following \"nickname\" already exists: ";
        assertEquals(message + createdUser.getNickname(), errorResponse.getMessage());
        AllureManager.addStep("Verify Error Message for Existing User", Status.PASSED, "Received Message: " + errorResponse.getMessage());


        AllureManager.cleanupThreadLocal();

    }


    @Test
    @Story("Create a New User with same Nick Name")
    @Description("Test to verify creating a new user with Existing Nick Name")
    @Severity(SeverityLevel.CRITICAL)
    @Order(5)
    public void createANewUser() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(7) + mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(existingUser.getNickname())
                .build();
        Response response = apiClient.createNewUser2(createdUser);
        AllureManager.captureResponse(response);

        assertEquals(409, response.statusCode());

        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        String message = "User with the following \"nickname\" already exists: ";
        assertEquals(message + createdUser.getNickname(), errorResponse.getMessage());
        AllureManager.addStep("Verify Error Message for Existing User", Status.PASSED, "Received Message: " + errorResponse.getMessage());


        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Order(6)
    @Story("Create a New User with Required Details")
    @Description("Test to verify creating a new user")
    @Severity(SeverityLevel.CRITICAL)
    public void createNewUser() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(10) + mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser2(createdUser);
        AllureManager.captureResponse(response);
        assertEquals(200, response.statusCode());
        UserModel userResponse = response.as(UserModel.class);
        sharedUser = userResponse;

        assertEquals(createdUser.getName(), userResponse.getName());
        AllureManager.addStep("Verify User Name", Status.PASSED, "Received User Name: " + userResponse.getName());

        assertEquals(createdUser.getNickname(), userResponse.getNickname());
        AllureManager.addStep("Verify User Nickname", Status.PASSED, "Received User Nickname: " + userResponse.getNickname());

        assertEquals("", userResponse.getAvatar_url());
        AllureManager.addStep("Verify User Avatar URL", Status.PASSED, "Received User Avatar URL: " + userResponse.getAvatar_url());

        assertTrue(BasicUtils.isValidUUID(userResponse.getUuid()));
        AllureManager.addStep("Verify User UUID", Status.PASSED, "Received User UUID: " + userResponse.getUuid());


        AllureManager.cleanupThreadLocal();

    }


}
