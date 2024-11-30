package tests.reviews;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class CreateReviewsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
