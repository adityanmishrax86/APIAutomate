package tests.reviews;

import base.TestBase;
import framework.clients.APIClient;
import framework.utils.AuthenticationUtils;

public class DeleteReviewsTests extends TestBase {
    private final APIClient apiClient = new APIClient(AuthenticationUtils.getBearerToken());
}
