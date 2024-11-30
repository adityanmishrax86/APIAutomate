package tests.games;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class GetGamesTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}