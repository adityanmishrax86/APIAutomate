package tests.reviews;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class UpdateReviewsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
