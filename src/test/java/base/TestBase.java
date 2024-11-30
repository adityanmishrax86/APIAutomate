package base;

import framework.config.ConfigurationManager;
import framework.utils.AuthenticationUtils;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.jupiter.api.BeforeAll;

public class TestBase {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = ConfigurationManager.getBaseUrl(false);
        AuthenticationUtils.setToken();
        RestAssured.filters(
                new AllureRestAssured(),
                new RequestLoggingFilter(),
                new ResponseLoggingFilter()
        );
    }
}
