package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PagedMenu implements Menu {
    private final Component inventoryTitle;
    private final String id;
    private final Map<Action, List<net.staticstudios.menus.action.Action>> actions;
    private final List<Button> buttons;
    private final MenuViewer viewer;
    private final MenuOptions options;
    private final Map<Character, Button> buttonMappings;
    private final String template;
    private final boolean shrinkToFit;
    private final char marker;
    private final Button fallback;
    private Inventory currentPageInventory;
    private int page;
    private int nextPageButtonIndex = -1;
    private int previousPageButtonIndex = -1;
    private List<Button[]> currentPageButtons;

    public PagedMenu(String id, MenuViewer viewer, Component inventoryTitle, @NotNull Map<Action, List<net.staticstudios.menus.action.Action>> actions, String template, char marker, Button fallback, boolean shrinkToFit, List<Button> buttons, @NotNull Map<Character, Button> buttonMappings, @NotNull MenuOptions options) {
        this.inventoryTitle = inventoryTitle;
        this.id = id;
        this.actions = actions;
        this.buttonMappings = buttonMappings;
        this.template = template;
        this.viewer = viewer;
        this.options = options;
        this.buttons = new ArrayList<>();
        this.buttons.addAll(buttons);
        this.page = 0;
        this.shrinkToFit = shrinkToFit;
        this.marker = marker;
        this.fallback = fallback;
    }

    @Override
    public void runActions(@NotNull Action action, @NotNull MenuViewer viewer) {
        actions.getOrDefault(action, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
    }

    @Override
    public void setButton(int slot, @NotNull Button button) {
        buttons.set(slot, button);

        //If the button is on the current page, update the inventory
        int currentPageStartIndex = page * getPlaceholderCount();
        int currentPageEndIndex = (page + 1) * getPlaceholderCount();

        if (slot >= currentPageStartIndex && slot < currentPageEndIndex) {
            updateInventory();
        }
    }

    public void addButton(@NotNull Button button) {
        buttons.add(button);

        //If the button is on the current page, update the inventory
        int currentPageStartIndex = page * getPlaceholderCount();
        int currentPageEndIndex = (page + 1) * getPlaceholderCount();

        if (buttons.size() >= currentPageStartIndex && buttons.size() < currentPageEndIndex) {
            updateInventory();
        } else if (buttons.size() == currentPageEndIndex) { //Update the inventory to add the next page button
            updateInventory();
        }
    }

    @Override
    public @NotNull Button getButton(int slot) {
        if (slot == nextPageButtonIndex) {
            return Button.NEXT_PAGE;
        } else if (slot == previousPageButtonIndex) {
            return page == 0 ? Button.BACK : Button.PREVIOUS_PAGE;
        } else {
            return currentPageButtons.get(slot / 9)[slot % 9];
        }
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

    public int getPage() {
        return page;
    }

    public void previousPage() {
        if (page > 0) {
            page--;
            actions.getOrDefault(Menu.Action.PREVIOUS_PAGE, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
            updateInventory();
        }
    }

    public void nextPage() {
        int maxPage = (int) Math.ceil((double) buttons.size() / getPlaceholderCount()) - 1;

        if (page < maxPage) {
            page++;
            actions.getOrDefault(Menu.Action.NEXT_PAGE, List.of()).forEach(menuAction -> menuAction.invoke(viewer));
            updateInventory();
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        if (currentPageInventory == null) { //won't be null if its from a back action
            updateInventory();
        }

        return currentPageInventory;
    }

    private int getPlaceholderCount() {
        return (int) template.chars().filter(c -> c == marker).count();
    }

    private void updateInventory() {
        boolean reachedEndOfPlaceholders = false;
        int skippedLines = 0;
        int nextPageButtonIndex = -1;
        int previousPageButtonIndex = -1;
        int buttonIndex = page * getPlaceholderCount();

        List<Button[]> lines = new ArrayList<>();
        for (int i = 0; i < template.length(); i += 9) {
            Button[] line = new Button[9];

            //Skip all lines containing placeholders
            if (reachedEndOfPlaceholders && shrinkToFit && template.substring(i, i + 9).chars().anyMatch(c -> c == marker)) {
                skippedLines++;
                continue;
            }

            for (int j = 0; j < 9; j++) {
                char c = template.charAt(i + j);
                if (c == marker) {
                    if (buttonIndex < buttons.size()) {
                        line[j] = buttons.get(buttonIndex);
                    } else {
                        line[j] = fallback;
                        reachedEndOfPlaceholders = true;
                    }
                    buttonIndex++;
                } else {
                    if (c == 'N') {
                        nextPageButtonIndex = i + j - skippedLines * 9;
                    } else if (c == 'P') {
                        previousPageButtonIndex = i + j - skippedLines * 9;
                    }
                    line[j] = buttonMappings.getOrDefault(c, options.defaultPlaceholder());
                }
            }
            lines.add(line);
        }

        int size = 9 * lines.size();

        //Recreate the inventory if the size or page number has changed
        if (currentPageInventory == null || size != currentPageInventory.getSize()) {
            Inventory prevInventory = currentPageInventory;
            currentPageInventory = Bukkit.createInventory(this, size, inventoryTitle);

            //Open the new inventory if the viewer is still viewing the old one
            if (prevInventory != null && viewer.getPlayer().getOpenInventory().getTopInventory().equals(prevInventory)) {
                viewer.getPlayer().openInventory(currentPageInventory);
            }
        }

        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < 9; j++) {
                Button button = lines.get(i)[j];
                ItemStack item = button.getItemRepresentation(viewer, this);
                currentPageInventory.setItem(i * 9 + j, item);
            }
        }

        //If we still have buttons left, add a next page button
        if (buttons.size() > (page + 1) * getPlaceholderCount()) {
            if (nextPageButtonIndex != -1) {
                currentPageInventory.setItem(nextPageButtonIndex, Button.NEXT_PAGE.getItemRepresentation(viewer, this));
            }
        }

        //If we are not on the first page, add a previous page button
        if (page > 0) {
            if (previousPageButtonIndex != -1) {
                currentPageInventory.setItem(previousPageButtonIndex, Button.PREVIOUS_PAGE.getItemRepresentation(viewer, this));
            }
        } else {
            if (previousPageButtonIndex != -1) {
                currentPageInventory.setItem(previousPageButtonIndex, Button.BACK.getItemRepresentation(viewer, this));
            }
        }

        this.nextPageButtonIndex = nextPageButtonIndex;
        this.previousPageButtonIndex = previousPageButtonIndex;
        this.currentPageButtons = lines;
    }
}
