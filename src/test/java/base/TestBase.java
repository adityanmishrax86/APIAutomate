package base;

import framework.clients.APIClient;
import framework.config.ConfigurationManager;
import framework.models.users.UserModel;
import framework.models.users.UserResponseModel;
import framework.utils.AuthenticationUtils;
import framework.utils.BasicUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import shared.SharedUser;

import java.util.ArrayList;
import java.util.List;


public class TestBase {

    public static void setUpInitialUser() {
        RestAssured.baseURI = ConfigurationManager.getProdUrl();
        List<UserModel> newUsers = new ArrayList<>();
        List<UserModel> newCreatedUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String mailDomain = "@test.com";
            UserModel createdUser = UserModel.builder()
                    .name(BasicUtils.generateRandomString(10))
                    .email(BasicUtils.generateRandomString(10) + mailDomain)
                    .password(BasicUtils.generateRandomString(7))
                    .nickname(BasicUtils.generateRandomString(10))
                    .build();

            APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
            Response response = apiClient.createNewUser(createdUser);
            if (response.statusCode() == 200) {
                SharedUser.existingUser = response.as(UserModel.class);
                System.out.println("User Added: " + SharedUser.existingUser.getUuid());
                newUsers.add(createdUser);
                newCreatedUsers.add(SharedUser.existingUser);
            } else {
                System.out.println(response.statusCode());
                if(!newUsers.isEmpty() && !newCreatedUsers.isEmpty())
                    break;
                else
                    System.exit(1);
            }
        }

        SharedUser.sharedUsers = newUsers;
        SharedUser.sharedCreatedUsers = newCreatedUsers;
        System.out.println("Initial User Setup Completed");


    }

    public static void setAllUsers() {
        RestAssured.baseURI = ConfigurationManager.getProdUrl();
        APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
        Response responseUsers = apiClient.getAllUsers();
        if (responseUsers.statusCode() == 200) {
            UserResponseModel userResponse = responseUsers.as(UserResponseModel.class);
            SharedUser.sharedCreatedUsers = userResponse.getUsers();
        }
    }

    public static void deleteUsers() {
        RestAssured.baseURI = ConfigurationManager.getProdUrl();
        APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

        Response responseUsers = apiClient.getAllUsers();
        if (responseUsers.statusCode() == 200) {
            UserResponseModel userResponse = responseUsers.as(UserResponseModel.class);
            List<UserModel> user = userResponse.getUsers();

            if (!user.isEmpty()) {
                for (UserModel user1 : user) {
                    Response responseDelete = apiClient.deleteUsers(user1.getUuid());
                    if (responseDelete.statusCode() == 204) {
                        System.out.println("User Deleted: " + user1.getUuid());
                    }
                }
            }

        }

    }

    @BeforeAll
    public static void setup() {
        AuthenticationUtils.setToken();
        deleteUsers();
        setUpInitialUser();

        RestAssured.baseURI = ConfigurationManager.getBaseUrl();
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
