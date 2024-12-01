package tests.games;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.games.GameResponseModel;
import framework.models.games.GamesModel;
import framework.utils.AuthenticationUtils;
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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SearchGamesTests extends TestBase {
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
    @Description("Search Game")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search Game")
    public void validateSearchGame() {
        GamesModel searchGame = SharedGames.sharedGames.get(0);
        Map<String, Object> kv = Map.of("query", searchGame.getTitle());
        Response response = apiClient.searchGameByName(kv);

        assertEquals(200, response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertNotNull(gameResponseModel);
        assertNotNull(gameResponseModel.getGames());
        assertEquals(searchGame.getTitle(), gameResponseModel.getGames().get(0).getTitle());
        assertEquals(searchGame.getUuid(), gameResponseModel.getGames().get(0).getUuid());
        assertEquals(searchGame.getPrice(), gameResponseModel.getGames().get(0).getPrice());
        assertEquals(searchGame.getCategory_uuids().get(0), gameResponseModel.getGames().get(0).getCategory_uuids().get(0));
        assertEquals(1, gameResponseModel.getGames().size());
        assertEquals(1, gameResponseModel.getMeta().getTotal());
    }

    @Test
    @Order(3)
    @Description("Search Game")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search Game")
    public void validateSearchGameWoParams() {
        Map<String, Object> kv = Map.of("", "");
        Response response = apiClient.searchGameByName(kv);

        assertEquals(200, response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertNotNull(gameResponseModel);
        assertNotNull(gameResponseModel.getGames());
        assertEquals(10, gameResponseModel.getGames().size());
        assertEquals(10, gameResponseModel.getMeta().getTotal());
    }

    @Test
    @Order(4)
    @Description("Search Game")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search Game")
    public void validateSearchGameWithOneValueParams() {
        Map<String, Object> kv = Map.of("query", "4");
        Response response = apiClient.searchGameByName(kv);

        assertEquals(200, response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertNotNull(gameResponseModel);
        assertNotNull(gameResponseModel.getGames());
        assertEquals(2, gameResponseModel.getGames().size());
        assertEquals(2, gameResponseModel.getMeta().getTotal());
        assertTrue(gameResponseModel.getGames().stream().anyMatch(game -> game.getTitle().contains("4")));
    }

    @Test
    @Order(5)
    @Description("Search Game")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search Game")
    public void validateSearchGameWithNoExistOneValueParams() {
        Map<String, Object> kv = Map.of("query", "1999");
        Response response = apiClient.searchGameByName(kv);

        assertEquals(200, response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertNotNull(gameResponseModel);
        assertNotNull(gameResponseModel.getGames());
        assertEquals(0, gameResponseModel.getGames().size());
        assertEquals(0, gameResponseModel.getMeta().getTotal());
    }



}
