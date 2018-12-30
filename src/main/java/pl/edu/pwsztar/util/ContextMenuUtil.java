package pl.edu.pwsztar.util;

import javafx.event.Event;
import javafx.scene.control.Control;
import javafx.scene.input.ContextMenuEvent;

public class ContextMenuUtil {

    public static void remove(Control... controls){
        for (Control control : controls) {
            control.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
        }
    }
}
