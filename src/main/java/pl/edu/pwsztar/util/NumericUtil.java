package pl.edu.pwsztar.util;

import java.math.BigDecimal;

public class NumericUtil {

    public static boolean isBigDecimal(String text) {
        try {
            new BigDecimal(text);

            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
