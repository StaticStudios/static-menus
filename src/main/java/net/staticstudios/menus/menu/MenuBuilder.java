package net.staticstudios.menus.menu;


import net.staticstudios.menus.viewer.MenuViewer;

public interface MenuBuilder {
    /**
     * Build the menu
     *
     * @param viewer The viewer to build the menu for
     * @return The menu
     */
    Menu build(MenuViewer viewer);
}
