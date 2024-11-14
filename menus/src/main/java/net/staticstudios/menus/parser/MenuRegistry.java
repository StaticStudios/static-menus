package net.staticstudios.menus.parser;

import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.menu.MenuBuilder;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.HashMap;
import java.util.Map;

/**
 * A registry of menus. This is mainly used to store menus loaded from files.
 */
public class MenuRegistry {
    private final Map<String, MenuBuilder> registry = new HashMap<>();

    /**
     * Register a {@link MenuBuilder} with an id.
     * The reason a {@link MenuBuilder} is used is because the menu will need to be
     * built specifically for the viewer.
     *
     * @param id      The id to register the menu with
     * @param builder The builder to register
     */
    public void register(String id, MenuBuilder builder) {
        if (registry.containsKey(id)) {
            throw new IllegalArgumentException("Menu with id " + id + " already exists");
        }

        System.out.println("Registering menu with id " + id);
        registry.put(id, builder);
    }

    /**
     * Clear the registry
     */
    public void clear() {
        registry.clear();
    }

    /**
     * Get a menu from the registry
     *
     * @param id     The id of the menu
     * @param viewer The viewer to build the menu for
     * @return The menu, or null if it does not exist
     */
    public Menu get(String id, MenuViewer viewer) {
        MenuBuilder builder = registry.get(id);

        if (builder == null) {
            return null;
        }

        return builder.build(viewer);
    }
}
