package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class SimpleMenuBuilder implements Cloneable, MenuBuilder {
    private final boolean mutable;
    private final Map<Menu.Action, List<ViewerAction>> actions = new HashMap<>();
    private String id;
    private int size = -1;
    private Component title;
    private List<Button> buttons;
    private MenuOptions options = new MenuOptions();


    protected SimpleMenuBuilder(boolean mutable) {
        this.mutable = mutable;
    }

    /**
     * Set the ID of the menu.
     * This is used to track history and for other internal purposes.
     *
     * @param id The ID
     * @return The builder
     */
    public SimpleMenuBuilder id(String id) {
        SimpleMenuBuilder builder = clone();
        builder.id = id;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public SimpleMenuBuilder title(Component title) {
        SimpleMenuBuilder builder = clone();
        builder.title = title;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public SimpleMenuBuilder title(String title) {
        SimpleMenuBuilder builder = clone();
        builder.title = StaticMenus.getMiniMessage().deserialize("<black>" + title);
        return builder;
    }

    /**
     * Set the size of the menu.
     *
     * @param size The size
     * @return The builder
     */
    public SimpleMenuBuilder size(int size) {
        SimpleMenuBuilder builder = clone();
        builder.size = size;
        builder.buttons = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            builder.buttons.add(null);
        }
        return builder;
    }

    /**
     * Add a button to the menu.
     *
     * @param button The button
     * @return The builder
     */
    public SimpleMenuBuilder addButton(Button button) {
        if (buttons == null) throw new IllegalStateException("Size must be set");
        if (buttons.size() >= size) throw new IllegalStateException("Too many buttons");
        SimpleMenuBuilder builder = clone();
        buttons.add(button);
        return builder;
    }

    /**
     * Set a button at a specific index.
     *
     * @param index  The index
     * @param button The button
     * @return The builder
     */
    public SimpleMenuBuilder setButton(int index, Button button) {
        if (buttons == null) throw new IllegalStateException("Size must be set");
        if (index >= size) throw new IllegalArgumentException("Index out of bounds");
        SimpleMenuBuilder builder = clone();
        buttons.set(index, button);
        return builder;
    }

    /**
     * Add an action to run when the menu is opened.
     *
     * @param action The action
     * @return The builder
     */
    public SimpleMenuBuilder onOpen(ViewerAction action) {
        SimpleMenuBuilder builder = clone();
        List<ViewerAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.OPEN, new ArrayList<>()));
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
    public SimpleMenuBuilder onClose(ViewerAction action) {
        SimpleMenuBuilder builder = clone();
        List<ViewerAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.CLOSE, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Menu.Action.CLOSE, actions);
        return builder;
    }

    /**
     * Set the options for the menu.
     *
     * @param optionsEditor A function to edit the options
     * @return The builder
     */
    public SimpleMenuBuilder options(Function<MenuOptions, MenuOptions> optionsEditor) {
        SimpleMenuBuilder builder = clone();
        builder.options = optionsEditor.apply(builder.options);
        return builder;
    }

    /**
     * Set the default placeholder button.
     *
     * @param defaultPlaceholder The default placeholder button
     * @return The builder
     */
    public SimpleMenuBuilder defaultPlaceholder(Button defaultPlaceholder) {
        SimpleMenuBuilder builder = clone();
        builder.options = builder.options.defaultPlaceholder(defaultPlaceholder);
        return builder;
    }

    @Override
    public Menu build(MenuViewer viewer) {
        if (id == null) throw new IllegalStateException("ID must be set");
        if (size == -1 || buttons == null) throw new IllegalStateException("Size must be set");
        if (title == null) throw new IllegalStateException("Title must be set");
        return new SimpleMenu(id, viewer, title, size, actions, buttons, options);
    }


    public SimpleMenuBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            return (SimpleMenuBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
