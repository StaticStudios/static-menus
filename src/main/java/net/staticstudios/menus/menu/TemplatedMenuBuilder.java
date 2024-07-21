package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.MenuAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class TemplatedMenuBuilder implements Cloneable, MenuBuilder {
    private final boolean mutable;
    private final Map<Menu.Action, List<MenuAction>> actions = new HashMap<>();
    private final int size;
    private final Map<Character, Button> buttonMappings = new HashMap<>();
    private final String template;
    private String id;
    private Component title;
    private MenuOptions options = new MenuOptions();

    protected TemplatedMenuBuilder(boolean mutable, String template) {
        this.mutable = mutable;
        this.template = template.replace("\n", "").replace("\r", "").replace("\t", "");
        size = this.template.length();
        if (size < 9 || size > 54 || size % 9 != 0) {
            throw new IllegalArgumentException("Size must be between 9 and 54 and divisible by 9");
        }

        //Default button mappings
        buttonMappings.put('B', Button.BACK);
    }

    /**
     * Set the ID of the menu.
     * This is used to track history and for other internal purposes.
     *
     * @param id The ID
     * @return The builder
     */
    public TemplatedMenuBuilder id(String id) {
        TemplatedMenuBuilder builder = clone();
        builder.id = id;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public TemplatedMenuBuilder title(Component title) {
        TemplatedMenuBuilder builder = clone();
        builder.title = title;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public TemplatedMenuBuilder title(String title) {
        TemplatedMenuBuilder builder = clone();
        builder.title = StaticMenus.getMiniMessage().deserialize("<black>" + title);
        return builder;
    }

    /**
     * Add an action to run when the menu is opened.
     *
     * @param action The action
     * @return The builder
     */
    public TemplatedMenuBuilder onOpen(MenuAction action) {
        TemplatedMenuBuilder builder = clone();
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
    public TemplatedMenuBuilder onClose(MenuAction action) {
        TemplatedMenuBuilder builder = clone();
        List<MenuAction> actions = new ArrayList<>(builder.actions.getOrDefault(Menu.Action.CLOSE, new ArrayList<>()));
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
    public TemplatedMenuBuilder options(Function<MenuOptions, MenuOptions> optionsEditor) {
        TemplatedMenuBuilder builder = clone();
        builder.options = optionsEditor.apply(builder.options);
        return builder;
    }

    /**
     * Set the default placeholder button.
     *
     * @param defaultPlaceholder The default placeholder button
     * @return The builder
     */
    public TemplatedMenuBuilder defaultPlaceholder(Button defaultPlaceholder) {
        TemplatedMenuBuilder builder = clone();
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
    public TemplatedMenuBuilder button(char character, Button button) {
        TemplatedMenuBuilder builder = clone();
        builder.buttonMappings.put(character, button);
        return builder;
    }

    @Override
    public Menu build(MenuViewer viewer) {
        if (id == null) throw new IllegalStateException("ID must be set");
        if (title == null) throw new IllegalStateException("Title must be set");

        return new TemplatedMenu(id, viewer, title, size, actions, template, buttonMappings, options);
    }

    public TemplatedMenuBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            return (TemplatedMenuBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
