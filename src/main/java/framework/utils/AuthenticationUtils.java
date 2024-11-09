package framework.utils;

import framework.config.ConfigurationManager;

public class AuthenticationUtils {
    public static String getBearerToken() {
        // Implement your token generation/retrieval logic
        return "your-token";
    }

    public static String getBasicAuth() {
        // Implement Basic Auth
        return "Basic " + "base64-encoded-credentials";
    }

    public static String getApiKey() {
        return ConfigurationManager.getApiKey();
    }
}
