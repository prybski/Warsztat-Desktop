package pl.edu.pwsztar.util;

public class NumericUtil {

    public static boolean isDouble(String text) {
        try {
            Double.parseDouble(text);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
