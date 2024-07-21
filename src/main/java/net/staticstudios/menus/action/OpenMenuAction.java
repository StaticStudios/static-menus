package net.staticstudios.menus.action;

import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.function.Function;

public class OpenMenuAction implements ButtonAction {
    private final Function<MenuViewer, Menu> menuToOpen;

    protected OpenMenuAction(Function<MenuViewer, Menu> menuSupplier) {
        this.menuToOpen = menuSupplier;
    }

    @Override
    public void invoke(MenuViewer viewer) {
        menuToOpen.apply(viewer).open();
    }
}
