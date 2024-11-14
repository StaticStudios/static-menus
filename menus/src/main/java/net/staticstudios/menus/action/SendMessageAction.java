package net.staticstudios.menus.action;

import net.staticstudios.menus.viewer.MenuViewer;

public class SendMessageAction implements ButtonAction {
    private final String message;

    protected SendMessageAction(String message) {
        this.message = message;
    }

    @Override
    public void invoke(MenuViewer viewer) {
        viewer.sendMessage(message);
    }
}
