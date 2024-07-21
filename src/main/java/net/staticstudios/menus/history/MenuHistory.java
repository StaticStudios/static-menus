package net.staticstudios.menus.history;

import net.staticstudios.menus.menu.Menu;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MenuHistory {
    private final List<Menu> history = new ArrayList<>();

    public void push(Menu menu) {
        history.add(menu);
    }

    public @Nullable Menu pop() {
        if (history.isEmpty()) {
            return null;
        }
        return history.removeLast();
    }

    public void replace(Menu menu) {
        if (history.isEmpty()) {
            history.add(menu);
            return;
        }
        history.set(history.size() - 1, menu);
    }

    public @Nullable Menu getLatest() {
        if (history.isEmpty()) {
            return null;
        }
        return history.getLast();
    }

    public void clear() {
        history.clear();
    }

    public int size() {
        return history.size();
    }
}
