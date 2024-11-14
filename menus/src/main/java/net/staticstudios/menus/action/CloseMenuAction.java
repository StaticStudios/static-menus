package net.staticstudios.menus.action;

import net.staticstudios.menus.viewer.MenuViewer;

public class CloseMenuAction implements ButtonAction {
    @Override
    public void invoke(MenuViewer viewer) {
        viewer.closeMenu();
    }
}
