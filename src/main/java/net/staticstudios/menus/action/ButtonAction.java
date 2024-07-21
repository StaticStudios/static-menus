package net.staticstudios.menus.action;

import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.function.Function;

public interface ButtonAction {
    static ButtonAction openMenu(Menu menu) {
        return new OpenMenuAction(menuViewer -> menu);
    }

    static ButtonAction openMenu(Function<MenuViewer, Menu> menuSupplier) {
        return new OpenMenuAction(menuSupplier);
    }

    static ButtonAction goBack() {
        return new GoBackAction();
    }

    void invoke(MenuViewer viewer);
}
