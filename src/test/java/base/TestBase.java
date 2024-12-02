package base;

import framework.clients.APIClient;
import framework.config.ConfigurationManager;
import framework.models.users.UserModel;
import framework.models.users.UserResponseModel;
import framework.utils.AuthenticationUtils;
import framework.utils.BasicUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.config.ConnectionConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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

    public static RestAssuredConfig configureConnectionHandling() {
        // Create a connection pool manager
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(100);  // Maximum total connections
        connectionManager.setDefaultMaxPerRoute(20);  // Max connections per route

        // Configure request timeout and connection settings
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)        // Connection timeout (5 seconds)
                .setSocketTimeout(10000)        // Socket read timeout (10 seconds)
                .setConnectionRequestTimeout(5000)  // Connection request timeout (5 seconds)
                .build();

        // Build HttpClient with retry and connection management
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler((exception, executionCount, context) -> {
                    // Retry mechanism for connection issues
                    if (executionCount > 3) {
                        return false;  // Stop after 3 retries
                    }
                    return exception instanceof java.net.ConnectException
                            || exception instanceof java.net.SocketException;
                })
                .build();

        // Configure REST Assured with custom HTTP client
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        );
    }

    @BeforeAll
    public static void setup() {
        AuthenticationUtils.setToken();
        deleteUsers();
        setUpInitialUser();
        RestAssured.config = configureConnectionHandling();
        RestAssured.baseURI = ConfigurationManager.getBaseUrl();
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
