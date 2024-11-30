package tests.orders;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class CreateOrderTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
