package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.users.UserModel;
import framework.utils.AuthenticationUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import shared.SharedUser;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateAvatarTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Description("Validate User Avatar API")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Update User Avatar API")
    public void updateAvatar() {
        String userID = SharedUser.sharedCreatedUsers.get(0).getUuid();
        File file = new File("src/test/resources/avatar.jpeg");
        Response response = apiClient.updateUserAvatar(userID, file);
        assertEquals(200, response.getStatusCode());
        UserModel updatedUser = response.as(UserModel.class);
        assertTrue(updatedUser.getAvatar_url().contains("https://gravatar.com/avatar/"));
    }
}
