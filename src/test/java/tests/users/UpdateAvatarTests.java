package tests.users;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class UpdateAvatarTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
