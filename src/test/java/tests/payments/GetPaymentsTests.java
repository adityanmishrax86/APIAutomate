package tests.payments;

import base.TestBase;
import framework.clients.APIClient;
import framework.models.games.GameResponseModel;
import framework.models.orders.OrderModel;
import framework.models.payments.PaymentsModel;
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
import shared.SharedOrders;
import shared.SharedPayments;
import shared.SharedUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GetPaymentsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());

    @Test
    @Description("Validate all Payments API")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create Orders for Payments")
    @Order(1)
    public void AddOrders() {
        SharedOrders.sharedOrders.clear();
        Map<String, Object> body;
        Response gameResponse = apiClient.getGames();
        assertEquals(gameResponse.getStatusCode(), 200);
        GameResponseModel gameResponseModel = gameResponse.as(GameResponseModel.class);
        assertFalse(gameResponseModel.getGames().isEmpty());
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Map<String, List<Map<String, Object>>> body1 = new HashMap<>();
        List<Map<String, Object>> body2 = new ArrayList<>();
        for(int k = 0; k < 5; k++) {
            for (int i = 1; i < 2; i++) {
                body = Map.of("item_uuid", gameResponseModel.getGames().get(k).getUuid(), "quantity", (k+1)*2);
                body2.add(body);

            }
            body1.put("items", body2);
            Response orderResponse = apiClient.addOrders(userId, body1);
            assertEquals(orderResponse.getStatusCode(), 200);
            OrderModel orderModel = orderResponse.as(OrderModel.class);
            SharedOrders.sharedOrders.add(orderModel);
        }

        for(OrderModel order : SharedOrders.sharedOrders) {
            String orderId = order.getUuid();
            Map<String, Object> body3 = Map.of("order_uuid", orderId, "payment_method", "card");
            Response paymentResponse = apiClient.createPayments(userId, body3);
            assertEquals(paymentResponse.getStatusCode(), 200);
            SharedPayments.sharedPayments.add(paymentResponse.as(PaymentsModel.class));
        }

    }

    @Test
    @Description("Validate all Payments API")
    @Severity(SeverityLevel.CRITICAL)
    @Story("List all payments of user")
    @Order(2)
    public void ListSinglePayment() {
        String paymentId = SharedPayments.sharedPayments.get(0).getUuid();
        String userId = SharedUser.sharedCreatedUsers.get(0).getUuid();
        Response paymentResponse = apiClient.getASinglePayment(paymentId);
        assertEquals(paymentResponse.getStatusCode(), 200);
        PaymentsModel paymentModel = paymentResponse.as(PaymentsModel.class);
        assertEquals(paymentId, paymentModel.getUuid());
        assertEquals(userId, paymentModel.getUser_uuid());
        assertEquals(SharedOrders.sharedOrders.get(0).getUuid(), paymentModel.getOrder_uuid());
        assertEquals("processing", paymentModel.getStatus());
        assertEquals("card", paymentModel.getPayment_method());
        assertNotNull(paymentModel.getCreated_at());
        assertNotNull(paymentModel.getUpdated_at());
    }



}
