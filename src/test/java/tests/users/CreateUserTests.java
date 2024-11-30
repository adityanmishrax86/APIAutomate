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
    @Order(2)
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

        if (response.statusCode() == 200) {
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

        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 200" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Order(3)
    @Story("Get User Details after Created")
    @Description("Test to verify getting User Details after it is created")
    @Severity(SeverityLevel.CRITICAL)
    public void validateUserCreatedOrNot() {
        Response response = apiClient.getAllUsers();
        // Add test step
        AllureManager.captureResponse(response);

        AllureManager.addStep("Verify response status", Status.PASSED,
                "Received Response Status: " + response.getStatusCode());

        if (response.statusCode() == 200) {
            UserResponseModel userResponse = response.as(UserResponseModel.class);
            List<UserModel> users = userResponse.getUsers();
            Meta meta = userResponse.getMeta();

            assertTrue(meta.getTotal() >= 0);
            AllureManager.addStep("Verify Total Meta Data", Status.PASSED,
                    "Received Meta Data: " + meta.getTotal());

            assertTrue(users.size() > 0);
            AllureManager.addStep("Verify Users Data", Status.PASSED,
                    "Received Users Data: " + users);

            if(null != sharedUser) {
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


        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Create a New User with same Email Address")
    @Description("Test to verify creating a new user with Existing Email Address")
    @Severity(SeverityLevel.CRITICAL)
    @Order(4)
    public void createUserWithSameEmail() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(existingUser.getEmail())
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 409) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "User with the following \"email\" already exists: ";
            assertEquals(message + createdUser.getEmail(), errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Existing User", Status.PASSED, "Received Message: " + errorResponse.getMessage());


        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 409" + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();

    }

    @Test
    @Story("Create a New User with same Nick Name")
    @Description("Test to verify creating a new user with Existing Nick Name")
    @Severity(SeverityLevel.CRITICAL)
    @Order(5)
    public void createUserWithSameNickName() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(7)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(existingUser.getNickname())
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 409) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "User with the following \"nickname\" already exists: ";
            assertEquals(message + createdUser.getNickname(), errorResponse.getMessage());
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
    @Order(6)
    public void createUserWithEmptyName() {
        UserModel createdUser = UserModel.builder()
                .name("")
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/name\": minimum string length is 1";
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
    @Order(7)
    public void createUserWithEmptyNickName() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname("")
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/nickname\": minimum string length is 2";
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
    @Order(8)
    public void createUserWithEmptyEmail() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email("")
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(12))
                .build();;
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);

        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/email\": minimum string length is 5";
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
    @Order(9)
    public void createUserWithEmptyPassword() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password("")
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);
        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/password\": minimum string length is 6";
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
    @Order(10)
    public void createUserWithPasswordLessThan6Characters() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password("Test")
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        AllureManager.captureResponse(response);
        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/password\": minimum string length is 6";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Password", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: \n Expected status: 400" + response.getStatusCode());
        }
        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Empty Body")
    @Description("Test to verify requesting to create Users with empty body")
    @Severity(SeverityLevel.CRITICAL)
    @Order(11)
    public void validateEmptyBodyRequest() {
        UserModel createdUser = UserModel.builder().build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/email\": property \"email\" is missing";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Empty Email Property")
    @Description("Test to verify requesting to create Users without Email Field")
    @Severity(SeverityLevel.CRITICAL)
    @Order(12)
    public void validateEmptyEmailRequest() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .password(BasicUtils.generateRandomString(10))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/email\": property \"email\" is missing";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Empty Email Property")
    @Description("Test to verify requesting to create Users without Password Field")
    @Severity(SeverityLevel.CRITICAL)
    @Order(13)
    public void validateEmptyPasswordRequest() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/password\": property \"password\" is missing";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Empty Name Property")
    @Description("Test to verify requesting to create Users without Name Field")
    @Severity(SeverityLevel.CRITICAL)
    @Order(14)
    public void validateEmptyNameRequest() {
        UserModel createdUser = UserModel.builder()
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .nickname(BasicUtils.generateRandomString(10))
                .password(BasicUtils.generateRandomString(12))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/name\": property \"name\" is missing";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Empty Nickname Property")
    @Description("Test to verify requesting to create Users without Name Field")
    @Severity(SeverityLevel.CRITICAL)
    @Order(15)
    public void validateEmptyNickNameRequest() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/nickname\": property \"nickname\" is missing";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }


    @Test
    @Story("Creating Nick name with Special Characters")
    @Description("Test to verify requesting to create Users with Nick name having Special Characters")
    @Severity(SeverityLevel.CRITICAL)
    @Order(16)
    public void validateNickNameWithSpecialCharacters() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(BasicUtils.generateRandomString(10)+"*,!@323234{}")
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/nickname\": string doesn't match the regular expression \"^[a-zA-Z0-9_.+-]+$\"";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Password more than 100 characters")
    @Description("Test to verify requesting to create Users with Password more than 100 characters")
    @Severity(SeverityLevel.CRITICAL)
    @Order(17)
    public void validatePasswordWithMoreThan100Char() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(107))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/password\": maximum string length is 100";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Nickname more than 100 characters")
    @Description("Test to verify requesting to create Users with Nickname more than 100 characters")
    @Severity(SeverityLevel.CRITICAL)
    @Order(18)
    public void validateNickNameWithMoreThan100Char() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(BasicUtils.generateRandomString(107))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/nickname\": maximum string length is 100";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Name more than 100 characters")
    @Description("Test to verify requesting to create Users with Name more than 100 characters")
    @Severity(SeverityLevel.CRITICAL)
    @Order(19)
    public void validateNameWithMoreThan100Char() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(107))
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/name\": maximum string length is 100";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

    @Test
    @Story("Request with Email more than 100 characters")
    @Description("Test to verify requesting to create Users with Email more than 100 characters")
    @Severity(SeverityLevel.CRITICAL)
    @Order(20)
    public void validateEmailWithMoreThan100Char() {
        UserModel createdUser = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(107)+mailDomain)
                .password(BasicUtils.generateRandomString(10))
                .nickname(BasicUtils.generateRandomString(10))
                .build();
        Response response = apiClient.createNewUser(createdUser);
        // Add test step
        AllureManager.captureResponse(response);


        if (response.statusCode() == 400) {
            ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
            String message = "request body has an error: doesn't match schema #/components/schemas/NewUser: Error at \"/email\": maximum string length is 100";
            assertEquals(message, errorResponse.getMessage());
            AllureManager.addStep("Verify Error Message for Empty Body", Status.PASSED, "Received Message: " + errorResponse.getMessage());
        } else {
            AllureManager.addStep("Verify response status", Status.FAILED,
                    "Received Response Status: " + response.getStatusCode());
        }

        AllureManager.cleanupThreadLocal();
    }

}
