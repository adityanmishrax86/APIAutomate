package tests.orders;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class DeleteOrderTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
