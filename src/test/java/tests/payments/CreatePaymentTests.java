package tests.payments;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class CreatePaymentTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
