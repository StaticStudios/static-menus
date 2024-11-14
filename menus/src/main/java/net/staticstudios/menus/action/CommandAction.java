package net.staticstudios.menus.action;

import net.staticstudios.menus.viewer.MenuViewer;

public class CommandAction implements ButtonAction {
    private final String command;

    protected CommandAction(String command) {
        this.command = command;
    }

    @Override
    public void invoke(MenuViewer viewer) {
        viewer.getPlayer().performCommand(command);
    }
}
