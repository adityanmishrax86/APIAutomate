package framework.utils;

import framework.config.ConfigurationManager;

public class AuthenticationUtils {

    protected static String token = "";

    public static String getBearerToken() {
        return token;
    }

    public static void setToken() {
        AuthenticationUtils.token = ConfigurationManager.getApiKey();
    }

   }
