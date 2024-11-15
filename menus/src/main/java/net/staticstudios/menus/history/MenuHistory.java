package net.staticstudios.menus.history;

import net.staticstudios.menus.menu.Menu;

import java.util.Stack;

public class MenuHistory extends Stack<Menu> {

    public void replace(Menu menu) {
        if (isEmpty()) {
            add(menu);
            return;
        }
        set(size() - 1, menu);
    }

}
