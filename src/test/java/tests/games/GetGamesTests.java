package tests.games;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.games.GameResponseModel;
import framework.models.games.GamesModel;
import framework.models.generic.ErrorResponseModel;
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
import shared.SharedGames;

import static org.junit.jupiter.api.Assertions.*;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetGamesTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Order(1)
    @Description("Get All GAmes")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Games")
    public void validateGetAllGames() {
        Response response = apiClient.getGames();
        if(response.statusCode()== 200){
            GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
            assertNotNull(gameResponseModel);
            SharedGames.sharedGames = gameResponseModel.getGames();
        }

    }

    @Test
    @Order(2)
    @Description("Get All GAmes")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Games")
    public void validateGetOneGame() {
        GamesModel gm = SharedGames.sharedGames.get(0);
        Response response = apiClient.getGameById(gm.getUuid());

        assertEquals(200, response.getStatusCode());

        GamesModel gameResponse = response.as(GamesModel.class);
        assertNotNull(gameResponse);
        assertEquals(gm.getUuid(), gameResponse.getUuid());
        assertEquals(gm.getTitle(), gameResponse.getTitle());
        assertEquals(gm.getPrice(), gameResponse.getPrice());
        assertEquals(gm.getCategory_uuids().get(0), gameResponse.getCategory_uuids().get(0));

    }

    @Test
    @Order(2)
    @Description("Get All GAmes")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Get Games")
    public void validateGetOneGameWithOutOtherUUID() {
        String nonGameUUID = BasicUtils.generateUUID();
        Response response = apiClient.getGameById(nonGameUUID);

        assertEquals(404, response.getStatusCode());

        ErrorResponseModel gameResponse = response.as(ErrorResponseModel.class);
        assertNotNull(gameResponse);
        assertEquals("Could not find game with \"uuid\": " + nonGameUUID, gameResponse.getMessage());

    }


}