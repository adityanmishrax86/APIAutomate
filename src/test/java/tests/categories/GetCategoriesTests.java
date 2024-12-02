package tests.categories;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.categories.CategoriesResponseModel;
import framework.models.games.GameResponseModel;
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
public class GetCategoriesTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Order(1)
    @Description("Validate all Categories APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Look for all Categories")
    public void validateGetAllCategories() {
        Response response = apiClient.getCategories();
        if(response.statusCode()== 200){
            CategoriesResponseModel gameResponseModel = response.as(CategoriesResponseModel.class);
            assertNotNull(gameResponseModel);
            SharedGames.sharedCategories = gameResponseModel.getCategories();
        }

    }

    @Test
    @Order(2)
    @Description("Validate all Categories APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Look for the Games as per the category")
    public void validateGetGamesAsPerCategory() {
        String categoryId = SharedGames.sharedCategories.get(0).getUuid();
        Response response = apiClient.getGamesAsPerCategory(categoryId);

        assertEquals(200, response.getStatusCode());
        GameResponseModel gameResponseModel = response.as(GameResponseModel.class);
        assertNotNull(gameResponseModel);
        assertEquals(gameResponseModel.getMeta().getTotal(), gameResponseModel.getGames().size());
        assertTrue(gameResponseModel.getGames().stream().allMatch(game -> game.getCategory_uuids().contains(categoryId)));

    }

    @Test
    @Order(3)
    @Description("Validate all Categories APIs")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Look for the games with invalid category")
    public void validateGetGamesAsPerInvalidCategory() {
        String categoryId = BasicUtils.generateUUID();
        Response response = apiClient.getGamesAsPerCategory(categoryId);

        assertEquals(404, response.getStatusCode());
        ErrorResponseModel error = response.as(ErrorResponseModel.class);
        assertNotNull(error);
        assertEquals("Could not find category with \"uuid\": "+categoryId, error.getMessage());

    }

}
