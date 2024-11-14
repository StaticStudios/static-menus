package net.staticstudios.menus.action;

import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.function.Function;

public interface ButtonAction extends Action {
    static ButtonAction openMenu(Menu menu) {
        return new OpenMenuAction(menuViewer -> menu);
    }

    static ButtonAction openMenu(Function<MenuViewer, Menu> menuSupplier) {
        return new OpenMenuAction(menuSupplier);
    }

    static ButtonAction closeMenu() {
        return new CloseMenuAction();
    }

    static ButtonAction goBack() {
        return new GoBackAction();
    }

    static ButtonAction sendMessage(String message) {
        return new SendMessageAction(message);
    }

    static ButtonAction command(String command) {
        return new CommandAction(command);
    }
}
