package framework.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream file = new FileInputStream("src/test/resources/config.properties");
            properties.load(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration properties", e);
        }
    }

    public static String getBaseUrl(boolean isProd) {
        return isProd ? properties.getProperty("base.url.prod") : properties.getProperty("base.url.dev");
    }

    public static String getProdUrl() {

        return properties.getProperty("base.url.prod");}
    public static String getApiKey() {
        return properties.getProperty("api.key");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
