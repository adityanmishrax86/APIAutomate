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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdateUsersTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
    private static final String mailDomain = "@test.com";
    @Test
    @Order(1)
    @Description("Update a user with valid details")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update User API")
    public void testUpdateUserWithValidDetails() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(10) + mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);
        assertEquals(requestBody.getEmail(), updatedUser.getEmail());
        assertEquals(requestBody.getName(), updatedUser.getName());
        assertEquals(requestBody.getNickname(), updatedUser.getNickname());
    }

    @Test
    @Order(2)
    @Description("Update a user with invalid UUID format")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserWithInvalidUUID() {
        String invalidUUID = "123-abc";
        UserModel requestBody = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(10) + mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();


        Response response = apiClient.updateUser(invalidUUID, requestBody);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("parameter \"user_uuid\" in path has an error: minimum string length is 36", errorResponse.getMessage());
    }

    @Test
    @Order(3)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserWithNonExistingUUID() {
        String nonExistingUUID = BasicUtils.generateUUID();
        UserModel requestBody = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .email(BasicUtils.generateRandomString(10) + mailDomain)
                .password(BasicUtils.generateRandomString(7))
                .nickname(BasicUtils.generateRandomString(10))
                .build();


        Response response = apiClient.updateUser(nonExistingUUID, requestBody);

        assertEquals(404, response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("Could not find user with \"uuid\": " + nonExistingUUID, errorResponse.getMessage());
    }

    @Test
    @Order(4)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserWithEmptyBody() {
        String nonExistingUUID = BasicUtils.generateUUID();
        UserModel requestBody = UserModel.builder()
                .build();

        Response response = apiClient.updateUser(nonExistingUUID, requestBody);

        assertEquals(404    , response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertEquals("Could not find user with \"uuid\": " + nonExistingUUID, errorResponse.getMessage());
    }

    @Test
    @Order(5)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyName() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .name(BasicUtils.generateRandomString(10))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);
        assertEquals(requestBody.getName(), updatedUser.getName());
    }

    @Test
    @Order(5)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyNickName() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .nickname(BasicUtils.generateRandomString(10))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);
        assertEquals(requestBody.getNickname(), updatedUser.getNickname());
    }

    @Test
    @Order(5)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyEmail() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .email(BasicUtils.generateRandomString(12)+mailDomain)
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);
        assertEquals(requestBody.getEmail(), updatedUser.getEmail());
    }

    @Test
    @Order(5)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyInvalidEmail() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .email(BasicUtils.generateRandomString(12))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(400, response.getStatusCode());
        ErrorResponseModel error = response.as(ErrorResponseModel.class);
        assertNotNull(error);
        assertEquals("request body has an error: doesn't match schema #/components/schemas/PatchUser: Error at \"/email\": string doesn't match the regular expression \"^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$\"",
    error.getMessage());
    }

    @Test
    @Order(6)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyAvatarURL() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .avatar_url("https://test.com/" + BasicUtils.generateRandomString(12))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);
        assertEquals("", updatedUser.getAvatar_url());
    }

    @Test
    @Order(7)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserOnlyPassword() {
        String userUUID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        UserModel requestBody = UserModel.builder()
                .password(BasicUtils.generateRandomString(14))
                .build();

        Response response = apiClient.updateUser(userUUID, requestBody);

        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertNotNull(updatedUser);

    }


}