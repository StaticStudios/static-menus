package net.staticstudios.menus.action;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;

public class GoBackAction implements ButtonAction {
    protected GoBackAction() {
    }

    @Override
    public void invoke(MenuViewer viewer) {
        StaticMenus.getHistory(viewer).pop();
        Menu menuToOpen = StaticMenus.getHistory(viewer).isEmpty() ? null : StaticMenus.getHistory(viewer).peek();

        if (menuToOpen == null) {
            return;
        }
        menuToOpen.open(false, false);
        ViewerAction onComeBack = menuToOpen.getOptions().onComeBack();
        if (onComeBack != null) {
            onComeBack.invoke(viewer);
        }
    }
}
