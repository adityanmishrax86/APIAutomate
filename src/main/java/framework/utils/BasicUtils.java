package framework.utils;

import java.util.UUID;
import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

public class BasicUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    public static boolean isValidUUID(String uuid) {
        try {
            UUID.fromString(uuid);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static String generateRandomString(int length) {
        if (length <= 4) {
            throw new IllegalArgumentException("Length must be greater than 4 to accommodate 'Test'.");
        }

        StringBuilder randomString = new StringBuilder("Test");
        for (int i = 0; i < length - 4; i++) {
            randomString.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return randomString.toString();
    }

    public static int generateRandomNumber(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("Max must be greater than Min");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1); // Include 'max'
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

}
