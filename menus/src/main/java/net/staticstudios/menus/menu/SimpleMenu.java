package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class SimpleMenu implements Menu {
    private final Component inventoryTitle;
    private final String id;
    private final Map<Action, List<ViewerAction>> actions;
    private final int size;
    private final List<Button> buttons;
    private final MenuViewer viewer;
    private final MenuOptions options;
    private Inventory inventory;

    public SimpleMenu(String id, MenuViewer viewer, Component inventoryTitle, int size, @NotNull Map<Action, List<ViewerAction>> actions, @NotNull List<Button> buttons, @NotNull MenuOptions options) {
        this.inventoryTitle = inventoryTitle;
        this.id = id;
        this.actions = actions;
        this.size = size;
        this.buttons = buttons;
        this.viewer = viewer;
        this.options = options;

        if (buttons.size() != size) {
            throw new IllegalArgumentException("The number of buttons must be equal to the size of the inventory");
        }

        for (int i = 0; i < buttons.size(); i++) {
            Button button = buttons.get(i);
            if (button == null) {
                buttons.set(i, options.defaultPlaceholder());
            }
        }
    }

    @Override
    public void runActions(@NotNull Action action, @NotNull MenuViewer viewer) {
        actions.getOrDefault(action, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
    }

    @Override
    public void setButton(int slot, @NotNull Button button) {
        buttons.set(slot, button);
        inventory.setItem(slot, button.getItemRepresentation(viewer, this));
    }

    @Override
    public @NotNull Button getButton(int slot) {
        return buttons.get(slot);
    }

    @Override
    public void open(boolean clearHistory, boolean pushToHistory) {
        if (clearHistory) {
            StaticMenus.getHistory(viewer).clear();
        }
        if (pushToHistory) {
            StaticMenus.getHistory(viewer).push(this);
        } else {
            StaticMenus.getHistory(viewer).replace(this);
        }

        actions.getOrDefault(Menu.Action.OPEN, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
        viewer.getPlayer().openInventory(getInventory());
    }

    @Override
    public @NotNull MenuViewer getViewer() {
        return viewer;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public MenuOptions getOptions() {
        return options;
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (inventory == null) { //won't be null if its from a back action
            this.inventory = Bukkit.createInventory(this, size, inventoryTitle);
            for (int i = 0; i < buttons.size(); i++) {
                Button button = buttons.get(i);
                buttons.set(i, button);
                inventory.setItem(i, button.getItemRepresentation(viewer, this));
            }
        }

        return inventory;
    }

    @Override
    public void tick() {
        buttons.forEach(Button::tick);
    }
}
