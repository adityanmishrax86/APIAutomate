package tests.wishlists;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class UpdateWishlistsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}