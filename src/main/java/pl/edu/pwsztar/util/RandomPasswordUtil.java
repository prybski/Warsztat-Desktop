package pl.edu.pwsztar.util;

import java.security.SecureRandom;

public class RandomPasswordUtil {

    private static final String DIGITS = "0123456789";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = UPPERCASE.toLowerCase();
    private static final String COMMON_CHARS = "!@#$%^&*";

    private static SecureRandom secureRandom;

    static {
        secureRandom = new SecureRandom();
    }

    public static String randomPassword(int length) {
        String allCharacters = DIGITS + UPPERCASE + LOWERCASE + COMMON_CHARS;
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(allCharacters.length());
            stringBuilder.append(allCharacters.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }
}
