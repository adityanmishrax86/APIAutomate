package tests.carts;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class GetCartsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
