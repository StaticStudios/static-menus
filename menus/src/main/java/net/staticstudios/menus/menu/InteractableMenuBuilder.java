package net.staticstudios.menus.menu;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.options.MenuOptions;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class InteractableMenuBuilder implements Cloneable, MenuBuilder {
    private final boolean mutable;
    private final Map<Menu.Action, List<ViewerAction>> actions = new HashMap<>();
    private final int size;
    private final Map<Character, Button> buttonMappings = new HashMap<>();
    private final String template;
    private final List<InteractableMenu.Filter> filters = new ArrayList<>();
    private final List<InteractableMenu.UpdateAction> updateAction = new ArrayList<>();
    private char marker;
    private String id;
    private Component title;
    private MenuOptions options = new MenuOptions();

    protected InteractableMenuBuilder(boolean mutable, String template) {
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
    public InteractableMenuBuilder id(String id) {
        InteractableMenuBuilder builder = clone();
        builder.id = id;
        return builder;
    }

    /**
     * Set the marker character for the menu.
     * This marks where players can interact with the menu.
     *
     * @param marker The marker character
     * @return The builder
     */
    public InteractableMenuBuilder marker(char marker) {
        InteractableMenuBuilder builder = clone();
        builder.marker = marker;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public InteractableMenuBuilder title(Component title) {
        InteractableMenuBuilder builder = clone();
        builder.title = title;
        return builder;
    }

    /**
     * Set the title of the menu.
     *
     * @param title The title
     * @return The builder
     */
    public InteractableMenuBuilder title(String title) {
        InteractableMenuBuilder builder = clone();
        builder.title = StaticMenus.getMiniMessage().deserialize("<black>" + title);
        return builder;
    }

    /**
     * Add an action to run when the menu is opened.
     *
     * @param action The action
     * @return The builder
     */
    public InteractableMenuBuilder onOpen(ViewerAction action) {
        InteractableMenuBuilder builder = clone();
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
    public InteractableMenuBuilder onClose(ViewerAction action) {
        InteractableMenuBuilder builder = clone();
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
    public InteractableMenuBuilder options(Function<MenuOptions, MenuOptions> optionsEditor) {
        InteractableMenuBuilder builder = clone();
        builder.options = optionsEditor.apply(builder.options);
        return builder;
    }

    /**
     * Add a button mapping.
     *
     * @param character The character to map
     * @param button    The button to map to
     * @return The builder
     */
    public InteractableMenuBuilder button(char character, Button button) {
        InteractableMenuBuilder builder = clone();
        builder.buttonMappings.put(character, button);
        return builder;
    }

    @Override
    public InteractableMenuBuilder onComeBack(ViewerAction callback) {
        InteractableMenuBuilder builder = clone();
        builder.options = builder.options.onComeBack(callback);
        return builder;
    }

    /**
     * Filter what items can be placed in the menu.
     *
     * @param filter The predicate to filter items
     * @return The builder
     */
    public InteractableMenuBuilder filter(Predicate<ItemStack> filter) {
        return filter((slot, item) -> filter.test(item));
    }

    /**
     * Filter what items can be placed in the menu.
     *
     * @param filter The filter to apply
     * @return The builder
     */
    public InteractableMenuBuilder filter(InteractableMenu.Filter filter) {
        InteractableMenuBuilder builder = clone();
        builder.filters.add(filter);
        return builder;
    }

    public InteractableMenuBuilder onUpdate(InteractableMenu.UpdateAction updateAction) {
        InteractableMenuBuilder builder = clone();
        builder.updateAction.add(updateAction);
        return builder;
    }

    @Override
    public Menu build(MenuViewer viewer) {
        if (id == null) throw new IllegalStateException("ID must be set");
        if (title == null) throw new IllegalStateException("Title must be set");
        if (marker == 0) throw new IllegalStateException("Marker must be set");

        return new InteractableMenu(id, viewer, title, size, actions, template, marker, buttonMappings, options, filters, updateAction);
    }

    public InteractableMenuBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            InteractableMenuBuilder clone = (InteractableMenuBuilder) super.clone();
            clone.actions.putAll(new HashMap<>(this.actions));
            clone.buttonMappings.putAll(new HashMap<>(this.buttonMappings));
            clone.filters.addAll(new ArrayList<>(this.filters));
            clone.options = this.options.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
