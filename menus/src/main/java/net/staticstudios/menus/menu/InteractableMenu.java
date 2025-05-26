package net.staticstudios.menus.menu;

import com.google.common.base.Preconditions;
import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class InteractableMenu implements Menu {
    private final Component inventoryTitle;
    private final String id;
    private final Map<Action, List<ViewerAction>> actions;
    private final int size;
    private final Button[] buttons;
    private final MenuViewer viewer;
    private final MenuOptions options;
    private final Map<Character, Button> buttonMappings;
    private final String template;
    private final char marker;
    private final List<Predicate<ItemStack>> itemPredicates;
    private final List<UpdateAction> updateActions;
    private Inventory inventory;

    public InteractableMenu(String id, MenuViewer viewer, Component inventoryTitle, int size, @NotNull Map<Action, List<ViewerAction>> actions, String template, char marker, @NotNull Map<Character, Button> buttonMappings, @NotNull MenuOptions options, List<Predicate<ItemStack>> itemPredicates, List<UpdateAction> updateActions) {
        this.inventoryTitle = inventoryTitle;
        this.id = id;
        this.actions = new HashMap<>(actions);
        this.size = size;
        this.buttonMappings = buttonMappings;
        this.template = template;
        this.marker = marker;
        this.viewer = viewer;
        this.options = options;
        this.buttons = new Button[size];
        this.itemPredicates = itemPredicates;
        this.updateActions = updateActions;
        Preconditions.checkArgument(buttonMappings.keySet().stream().noneMatch(c -> marker == c), "Marker character cannot be used as a button mapping key");

        // Ensure that on close they get their items back
        this.actions.computeIfAbsent(Action.CLOSE, k -> new ArrayList<>()).add(v -> {
            Player player = v.getPlayer();
            for (ItemStack item : getItems()) {
                player.getInventory().addItem(item);
            }
        });

        //todo: add an action for when the menu is updated (items are updated)
    }

    @Override
    public void runActions(@NotNull Action action, @NotNull MenuViewer viewer) {
        actions.getOrDefault(action, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
    }

    @Override
    public void setButton(int slot, @NotNull Button button) {
        buttons[slot] = button;
        inventory.setItem(slot, button.getItemRepresentation(viewer, this));
    }

    public void setButton(char character, @NotNull Button button) {
        for (int i = 0; i < template.length(); i++) {
            if (template.charAt(i) == character) {
                setButton(i, button);
            }
        }
    }

    @Override
    public @NotNull Button getButton(int slot) {
        return buttons[slot] != null ? buttons[slot] : Button.EMPTY;
    }

    public boolean isInteractable(int slot) {
        return template.charAt(slot) == marker;
    }

    public boolean isInteractable(ItemStack item) {
        return itemPredicates.stream().allMatch(predicate -> predicate.test(item));
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

        actions.getOrDefault(Action.OPEN, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
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
            for (int i = 0; i < buttons.length; i++) {
                if (buttons[i] == null) {
                    Button button = buttonMappings.getOrDefault(template.charAt(i), options.defaultPlaceholder());
                    buttons[i] = button;
                    inventory.setItem(i, button.getItemRepresentation(viewer, this));
                }
            }
        }

        return inventory;
    }

    @Override
    public void tick() {
        for (Button button : buttons) {
            if (button != null) {
                button.tick();
            }
        }
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < buttons.length; i++) {
            if (template.charAt(i) == marker) {
                ItemStack item = inventory.getItem(i);
                if (item != null) {
                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * Clear all non-button items from the menu.
     */
    public void clearItems() {
        for (int i = 0; i < buttons.length; i++) {
            if (template.charAt(i) == marker) {
                inventory.setItem(i, null);
            }
        }
        callUpdateActions();
    }

    public void callUpdateActions() {
        for (UpdateAction action : updateActions) {
            action.apply(this, viewer, getItems());
        }
    }

    public interface UpdateAction {
        void apply(InteractableMenu menu, MenuViewer viewer, List<ItemStack> items);
    }
}
