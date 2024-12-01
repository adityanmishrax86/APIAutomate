package tests;

import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SelectClasses;
import tests.carts.GetCartsTests;
import tests.categories.GetCategoriesTests;
import tests.games.GetGamesTests;
import tests.games.SearchGamesTests;
import tests.orders.GetOrdersTests;
import tests.payments.GetPaymentsTests;
import tests.users.*;
import tests.wishlists.RemoveWishlistsTests;
import tests.wishlists.UpdateWishlistsTests;

@Suite
@SelectClasses({
        CreateUserTests.class,
        ListUsersTests.class,
        GetUserTests.class,
        LoginUserTests.class,
        UpdateUsersTests.class,
        UpdateAvatarTests.class,
        GetCategoriesTests.class,
        GetGamesTests.class,
        SearchGamesTests.class,
        GetCartsTests.class,
        UpdateWishlistsTests.class,
        RemoveWishlistsTests.class,
        GetOrdersTests.class,
        GetPaymentsTests.class,
        DeleteUsersTests.class,
})
public class OrderedTestSuite {

}
