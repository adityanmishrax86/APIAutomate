package framework.constants;

public class Endpoints {
    public static final String USERS = "/api/v1/users";
    public static final String USER_BY_ID = USERS + "/{userId}";
    public static final String USER_ORDERS = USER_BY_ID + "/orders";

    // Authentication endpoints
    public static final String LOGIN = "/api/v1/auth/login";
    public static final String LOGOUT = "/api/v1/auth/logout";
    public static final String REFRESH_TOKEN = "/api/v1/auth/refresh";

    // Product endpoints
    public static final String PRODUCTS = "/api/v1/products";
    public static final String PRODUCT_BY_ID = PRODUCTS + "/{productId}";

    // Order endpoints
    public static final String ORDERS = "/api/v1/orders";
    public static final String ORDER_BY_ID = ORDERS + "/{orderId}";

    // Health check
    public static final String HEALTH = "/health";
    public static final String STATUS = "/status";
}
