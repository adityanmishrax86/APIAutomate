package framework.config;

public enum Environment {
    DEV,
    QA,
    STAGING,
    PROD;

    public static Environment getCurrentEnvironment() {
        String env = System.getProperty("env", "QA");
        return Environment.valueOf(env.toUpperCase());
    }
}
