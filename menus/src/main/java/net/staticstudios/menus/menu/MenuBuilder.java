package net.staticstudios.menus.menu;


import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.viewer.MenuViewer;

public interface MenuBuilder {
    /**
     * Build the menu
     *
     * @param viewer The viewer to build the menu for
     * @return The menu
     */
    Menu build(MenuViewer viewer);

    /**
     * Specify a callback to be called when the menu is opened via the back button
     *
     * @param callback the callback to be called
     */
    MenuBuilder onComeBack(ViewerAction callback);
}
