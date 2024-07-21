package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.MenuAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.*;
import java.util.function.Function;

public class PagedMenuBuilder implements Cloneable, MenuBuilder {
    private final boolean mutable;
    private final Map<Menu.Action, List<MenuAction>> actions = new HashMap<>();
    private final Map<Character, Button> buttonMappings = new HashMap<>();
    private final List<Button> buttons = new ArrayList<>();
    private final String template;
    private String id;
    private Component title;
    private MenuOptions options = new MenuOptions();
    private boolean shrinkToFit = false;
    private char marker;
    private Button fallback = Button.EMPTY;

    protected PagedMenuBuilder(boolean mutable, String template) {
        this.mutable = mutable;
        this.template = template.replace("\n", "").replace("\r", "").replace("\t", "");
        int size = this.template.length();
        if (size < 9 || size > 54 || size % 9 != 0) {
            throw new IllegalArgumentException("Size must be between 9 and 54 and divisible by 9");
        }
    }

    /**
     * Set the ID of the menu.
     * This is used to track history and for other internal purposes.
     *
     * @param id The ID
     * @return The builder
     */
    public PagedMenuBuilder id(String id) {
        PagedMenuBuilder builder = clone();
        builder.id = id;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public PagedMenuBuilder title(Component title) {
        PagedMenuBuilder builder = clone();
        builder.title = title;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public PagedMenuBuilder title(String title) {
        PagedMenuBuilder builder = clone();
        builder.title = StaticMenus.getMiniMessage().deserialize("<black>" + title);
        return builder;
    }

    /**
     * Add an action to run when the menu is opened.
     *
     * @param action The action
     * @return The builder
     */
    public PagedMenuBuilder onOpen(MenuAction action) {
        PagedMenuBuilder builder = clone();
        List<MenuAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.OPEN, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Menu.Action.OPEN, actions);
        return builder;
    }

    /**
     * Add an action to run when the menu is closed.
     *
     * @param action The action
     * @return The builder
     */
    public PagedMenuBuilder onClose(MenuAction action) {
        PagedMenuBuilder builder = clone();
        List<MenuAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.CLOSE, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Menu.Action.CLOSE, actions);
        return builder;
    }

    /**
     * Add an action to run when the next page is opened.
     *
     * @param action The action
     * @return The builder
     */
    public PagedMenuBuilder onNextPage(MenuAction action) {
        PagedMenuBuilder builder = clone();
        List<MenuAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.NEXT_PAGE, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Menu.Action.NEXT_PAGE, actions);
        return builder;
    }

    /**
     * Add an action to run when the previous page is opened.
     *
     * @param action The action
     * @return The builder
     */
    public PagedMenuBuilder onPreviousPage(MenuAction action) {
        PagedMenuBuilder builder = clone();
        List<MenuAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.PREVIOUS_PAGE, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Menu.Action.PREVIOUS_PAGE, actions);
        return builder;
    }

    /**
     * Set the options for the menu.
     *
     * @param optionsEditor A function to edit the options
     * @return The builder
     */
    public PagedMenuBuilder options(Function<MenuOptions, MenuOptions> optionsEditor) {
        PagedMenuBuilder builder = clone();
        builder.options = optionsEditor.apply(builder.options);
        return builder;
    }

    /**
     * Set the default placeholder button.
     *
     * @param defaultPlaceholder The default placeholder button
     * @return The builder
     */
    public PagedMenuBuilder defaultPlaceholder(Button defaultPlaceholder) {
        PagedMenuBuilder builder = clone();
        builder.options = builder.options.defaultPlaceholder(defaultPlaceholder);
        return builder;
    }

    /**
     * Add a button mapping.
     *
     * @param character The character to map
     * @param button    The button to map to
     * @return The builder
     */
    public PagedMenuBuilder button(char character, Button button) {
        if (character == 'P' || character == 'N') {
            throw new IllegalArgumentException("P and N are reserved characters");
        }

        PagedMenuBuilder builder = clone();
        builder.buttonMappings.put(character, button);
        return builder;
    }

    /**
     * Set the buttons for the menu.
     *
     * @param buttons The buttons
     * @return The builder
     */
    public PagedMenuBuilder buttons(List<Button> buttons) {
        PagedMenuBuilder builder = clone();
        builder.buttons.addAll(buttons);
        return builder;
    }

    /**
     * Set the buttons for the menu.
     *
     * @param buttons The buttons
     * @return The builder
     */
    public PagedMenuBuilder buttons(Button... buttons) {
        PagedMenuBuilder builder = clone();
        builder.buttons.addAll(Arrays.asList(buttons));
        return builder;
    }

    /**
     * Set whether the menu should shrink to fit the buttons.
     *
     * @param shrinkToFit Whether the menu should shrink to fit the buttons
     * @return The builder
     */
    public PagedMenuBuilder shrinkToFit(boolean shrinkToFit) {
        PagedMenuBuilder builder = clone();
        builder.shrinkToFit = shrinkToFit;
        return builder;
    }

    /**
     * Sets the marker character
     *
     * @param marker The character to use as a marker
     * @return The builder
     */
    public PagedMenuBuilder marker(char marker) {
        PagedMenuBuilder builder = clone();
        builder.marker = marker;
        return builder;
    }

    /**
     * Sets the marker and the empty marker
     *
     * @param marker   The character to use as a marker
     * @param fallback The button to use as a fallback in place of a marker if there are no more buttons to show, but there are still placeholders
     * @return The builder
     */
    public PagedMenuBuilder marker(char marker, Button fallback) {
        PagedMenuBuilder builder = clone();
        builder.marker = marker;
        builder.fallback = fallback;
        return builder;
    }

    @Override
    public PagedMenu build(MenuViewer viewer) {
        if (id == null) throw new IllegalStateException("ID must be set");
        if (title == null) throw new IllegalStateException("Title must be set");
        if (marker == 0) throw new IllegalStateException("Placeholder must be set");

        return new PagedMenu(id, viewer, title, actions, template, marker, fallback, shrinkToFit, buttons, buttonMappings, options);
    }

    public PagedMenuBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            return (PagedMenuBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
