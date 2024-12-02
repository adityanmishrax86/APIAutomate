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
    public void testUpdateUserExistingEmailAddress() {
        String userUUID = SharedUser.sharedCreatedUsers.get(SharedUser.sharedUsers.size()-1).getUuid();
        String emailId = SharedUser.sharedUsers.get(3).getEmail();
        UserModel requestBody = UserModel.builder()
                .email(emailId)
                .build();;

        Response response = apiClient.updateUser2(userUUID, requestBody);

        assertEquals(409    , response.getStatusCode());
        ErrorResponseModel errorResponse = response.as(ErrorResponseModel.class);
        assertNotNull(errorResponse);
        assertEquals("User with the following \"email\" already exists: "+emailId, errorResponse.getMessage());
    }

    @Test
    @Order(5)
    @Description("Update a user with non-existing UUID")
    @Severity(SeverityLevel.NORMAL)
    @Story("Update User API")
    public void testUpdateUserExistingNickName() {
        String userUUID = SharedUser.sharedCreatedUsers.get(SharedUser.sharedUsers.size()-1).getUuid();
        String nickName = SharedUser.sharedUsers.get(3).getNickname();
        UserModel requestBody = UserModel.builder()
                .nickname(nickName)
                .build();

        Response response = apiClient.updateUser2(userUUID, requestBody);

        assertEquals(409, response.getStatusCode());
        ErrorResponseModel updatedUser = response.as(ErrorResponseModel.class);
        assertNotNull(updatedUser);
        assertEquals("User with the following \"nickname\" already exists: "+nickName, updatedUser.getMessage());
    }



}