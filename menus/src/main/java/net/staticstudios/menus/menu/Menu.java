package net.staticstudios.menus.menu;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.history.MenuHistory;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface Menu extends InventoryHolder {
    /**
     * Get a selector
     *
     * @return a builder selector
     */
    static MenuBuilderSelector builder() {
        return new MenuBuilderSelector();
    }

    /**
     * Run the actions associated with the given action (type)
     *
     * @param action the action (type) to run
     * @param viewer the viewer that triggered the action
     */
    void runActions(@NotNull Action action, @NotNull MenuViewer viewer);

    /**
     * Set the button at the given slot
     *
     * @param slot   the slot to set the button at
     * @param button the button to set
     */
    void setButton(int slot, @NotNull Button button);

    /**
     * Get the button at the given slot
     *
     * @param slot the slot to get the button from
     * @return the button at the given slot
     */
    @NotNull
    Button getButton(int slot);

    /**
     * Update the button at the given slot
     *
     * @param slot    the slot to update the button at
     * @param updater the function to update the button
     */
    default void updateButton(int slot, Function<@NotNull Button, @NotNull Button> updater) {
        Button button = getButton(slot);

        setButton(slot, updater.apply(button));
    }

    /**
     * Open the menu
     */
    default void open() {
        MenuHistory history = StaticMenus.getHistory(getViewer());
        Menu previous = history.isEmpty() ? null : history.peek();
        open(false, previous == null || !previous.getId().equals(this.getId())); //if we are opening a new menu, push it to the history
    }

    /**
     * Close the menu
     */
    default void close() {
        getViewer().getPlayer().closeInventory();
    }

    /**
     * Open the menu
     *
     * @param clearHistory  whether to clear the history
     * @param pushToHistory whether to push the menu to the history
     */
    void open(boolean clearHistory, boolean pushToHistory);

    /**
     * Get the viewer of the menu
     *
     * @return the viewer of the menu
     */
    @NotNull
    MenuViewer getViewer();

    /**
     * Get the ID of the menu
     *
     * @return the ID of the menu
     */
    String getId();

    /**
     * Get the options of the menu
     *
     * @return the options of the menu
     */
    MenuOptions getOptions();

    enum Action {
        OPEN,
        CLOSE,
        NEXT_PAGE,
        PREVIOUS_PAGE
    }
}
