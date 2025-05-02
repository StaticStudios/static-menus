package net.staticstudios.menus.button;

import java.util.function.Consumer;

public class ButtonUpdateAction<B extends Button> {
    private final int interval;
    private final Consumer<B> action;
    private int ticks = 0;

    public ButtonUpdateAction(int interval, Consumer<B> action) {
        this.interval = interval;
        this.action = action;
    }

    public boolean tick(B button) {
        if (ticks++ % interval == 0) {
            action.accept(button);
            return true;
        }
        return false;
    }
}
