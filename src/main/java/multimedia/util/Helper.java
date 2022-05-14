package multimedia.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Static helper class that provides capitalization of Strings and a random number generator.
 */
public class Helper {
    /**
     * Helper function that capitalizes the first letter of a word.
     *
     * @param str input string
     * @return capitalized string
     */
    public static String capitalize(String str) {
        if (str == null) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Helper function that generates a random number in the range [min, max].
     *
     * @param min minimum possible number
     * @param max maximum possible number
     * @return a random number between the two given arguments
     */
    public static int generateRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}