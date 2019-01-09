package pl.edu.pwsztar.util;

public class TruncateStringUtil {

    public static String truncate(String text, int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength) + "...)";
    }
}
