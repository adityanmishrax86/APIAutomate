package tests;

import base.TestBase;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DummyJsonUser extends TestBase {


//    @Test
//    @Story("Login User")
//    @Description("Test to validate Login User")
//    @Severity(SeverityLevel.CRITICAL)
//    public void loginUser() {
//        Response response = APIClient.loginUser(LoginModel.createLoginUser("emilys","emilyspass"));
//
//        AllureManager.captureResponse(response);
//
//        assertEquals(response.statusCode(),200);
//
//        AuthenticationUtils.setToken(response.jsonPath().getString("accessToken"));
//    }
//
//
//    @Test
//    @Story("Current Auth user")
//    @Description("Get the Current Auth User Details using Authentication Bearer token")
//    @Severity(SeverityLevel.CRITICAL)
//    public void getCurrentAuthUser() {
//        String token = AuthenticationUtils.getBearerToken();
//        Response response =APIClient.getCurrentAuthUser(token);
//        AllureManager.captureResponse(response);
//
//        assertEquals(response.statusCode(),200);
//
//        CurrentAuthUserModel user = response.as(CurrentAuthUserModel.class);
//        assertEquals("emilys", user.getUsername());
//        assertEquals("Emily", user.getFirstName());
//
//
//    }
//
//    @Test
//    @Story("Products")
//    @Description("Get All the Products")
//    @Severity(SeverityLevel.NORMAL)
//    public void getAllProducts() {
//        String token = AuthenticationUtils.getBearerToken();
//        Response response = APIClient.getAllTheProducts(token);
//        AllureManager.captureResponse(response);
//
//        assertEquals(response.statusCode(),200);
//
//
//    }
//
//    @Test
//    @Story("Products")
//    @Description("Get Single Product")
//    @Severity(SeverityLevel.NORMAL)
//    public void getASingleProduct() {
//        String token = AuthenticationUtils.getBearerToken();
//        String[] productId = {"1","2","3"};
//        for(String id : productId) {
//            Response response = APIClient.getProductById(id, token);
//            AllureManager.captureResponse(response);
//            assertEquals(response.statusCode(),200);
//        }
//    }
//
//    @Test
//    @Story("Products")
//    @Description("Get Product by other than ID")
//    @Severity(SeverityLevel.NORMAL)
//    public void getSingleProductOtherThanId() {
//        String token = AuthenticationUtils.getBearerToken();
//        String[] productId = {"asdabd","....","1=1"};
//        for(String id : productId) {
//            Response response = APIClient.getProductById(id, token);
//            AllureManager.captureResponse(response);
//            assertEquals(response.statusCode(),404);
//        }
//    }
//
//    @Test
//    @Story("Products")
//    @Description("Get Sorted Products")
//    @Severity(SeverityLevel.NORMAL)
//    public void getSortedProducts() {
//        String token = AuthenticationUtils.getBearerToken();
//        String[] productTags = {"price","stock","name"};
//        String[] orders = {"asc","desc","asc"};
//        for(int i = 0; i < productTags.length; i++) {
//            Response response = APIClient.getSortedProducts(productTags[i], orders[i], token);
//            AllureManager.captureResponse(response);
//            assertEquals(response.statusCode(),200);
//        }
//    }

//    @Test
//    @Story("Products")
//    @Description("Get Sorted Products")
//    @Severity(SeverityLevel.NORMAL)
//    public void getSortedProducts() {
//        String token = AuthenticationUtils.getBearerToken();
//        String[] productTags = {"price","stock","name"};
//        String[] orders = {"asc","desc","asc"};
//        for(int i = 0; i < productTags.length; i++) {
//            Response response = APIClient.getSortedProducts(productTags[i], orders[i], token);
//            AllureManager.captureResponse(response);
//            assertEquals(response.statusCode(),200);
//        }
//    }

}
