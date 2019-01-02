package pl.edu.pwsztar.util;

import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class DataFieldsUtil {

    private static final short DEFAULT_PRODUCTION_YEAR;
    private static final float DEFAULT_ENGINE_CAPACITY;

    static {
        DEFAULT_PRODUCTION_YEAR = 2000;
        DEFAULT_ENGINE_CAPACITY = 0.6f;
    }

    public static void resetFieldsToDefaults(Spinner<Integer> integerSpinner, Spinner<Double>
            doubleSpinner, TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }

        integerSpinner.getEditor().setText(String.valueOf(DEFAULT_PRODUCTION_YEAR));
        doubleSpinner.getEditor().setText(String.valueOf(DEFAULT_ENGINE_CAPACITY));
    }

    public static void clearDatePicker(DatePicker datePicker) {
        datePicker.setValue(null);
    }

    public static void clearTextArea(TextArea... textAreas) {
        for (TextArea textArea : textAreas) {
            textArea.clear();
        }
    }
}
