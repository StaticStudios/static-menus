package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ButtonAction;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Represents a button which can be modified after its creation
 */
public class MutableButton implements Button {
    private final ItemStack itemStack;
    private final Map<Action, List<ButtonAction>> actions;

    /**
     * Create a new mutable button
     *
     * @param source  the source item stack
     * @param actions the actions
     */
    protected MutableButton(ItemStack source, Map<Action, List<ButtonAction>> actions) {
        this.itemStack = new ItemStack(source);
        this.actions = actions;
    }

    /**
     * Set the name of the button
     *
     * @param name the name
     */
    public void setName(Component name) {
        itemStack.editMeta(meta -> meta.displayName(name));
    }

    /**
     * Edit the name of the button
     *
     * @param nameEditor the name editor
     */
    public void editName(@NotNull Function<Component, Component> nameEditor) {
        itemStack.editMeta(meta -> meta.displayName(nameEditor.apply(meta.displayName())));
    }

    /**
     * Edit the name of the button.
     * Note that this method will serialize the name to a string and then deserialize it back to a component
     *
     * @param nameEditor the name editor
     */
    public void editNameAsString(@NotNull Function<String, String> nameEditor) {
        Component displayName = itemStack.getItemMeta().displayName();
        String name = displayName == null ? "" : StaticMenus.getMiniMessage().serialize(displayName);

        itemStack.editMeta(meta -> meta.displayName(StaticMenus.getMiniMessage().deserialize(nameEditor.apply(name))));
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     */
    public void setDescription(String... description) {
        itemStack.editMeta(meta -> meta.lore(Stream.of(description).map(StaticMenus.getMiniMessage()::deserialize).toList()));
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     */
    public void setDescription(Component... description) {
        itemStack.editMeta(meta -> meta.lore(List.of(description)));
    }

    @Override
    public void componentDescription(List<Component> description) {
        itemStack.editMeta(meta -> meta.lore(description));
    }

    /**
     * Edit the description of the button
     *
     * @param descriptionEditor the description editor
     */
    public void editDescription(@NotNull Function<List<Component>, List<Component>> descriptionEditor) {
        List<Component> lore = itemStack.getItemMeta().lore();
        itemStack.editMeta(meta -> meta.lore(descriptionEditor.apply(lore == null ? new ArrayList<>() : new ArrayList<>(lore))));
    }

    /**
     * Edit the description of the button.
     * Note that this method will serialize the lore to strings and then deserialize them back to components
     *
     * @param descriptionEditor the description editor
     */
    public void editDescriptionAsStrings(@NotNull Function<List<String>, List<String>> descriptionEditor) {
        List<Component> lore = itemStack.getItemMeta().lore();
        List<String> loreStrings = lore == null ? new ArrayList<>() : new ArrayList<>(lore.stream().map(StaticMenus.getMiniMessage()::serialize).toList());

        itemStack.editMeta(meta -> meta.lore(descriptionEditor.apply(loreStrings).stream().map(StaticMenus.getMiniMessage()::deserialize).toList()));
    }

    /**
     * Set the count of the button
     *
     * @param count the count
     */
    public void setCount(int count) {
        itemStack.editMeta(meta -> meta.setMaxStackSize(99));

        itemStack.setAmount(count);
    }

    /**
     * Add an action to the button
     *
     * @param action       the action (type)
     * @param buttonAction the action (to run)
     */
    public void addAction(Action action, ButtonAction buttonAction) {
        actions.computeIfAbsent(action, a -> new ArrayList<>()).add(buttonAction);
    }

    /**
     * Set the actions of the button
     *
     * @param action        the action (type)
     * @param buttonActions the actions (to run)
     */
    public void setActions(Action action, List<ButtonAction> buttonActions) {
        actions.put(action, buttonActions);
    }

    /**
     * Clear all actions of the button
     */
    public void clearActions() {
        actions.clear();
    }

    /**
     * Clear all actions of the button for a specific action (type)
     *
     * @param action the action (type)
     */
    public void clearActions(Action action) {
        actions.remove(action);
    }

    /**
     * Add an action to the button for a left click
     *
     * @param buttonAction the action (to run)
     */
    public void onLeftClick(ButtonAction buttonAction) {
        addAction(Action.LEFT_CLICK, buttonAction);
    }

    /**
     * Add an action to the button for a right click
     *
     * @param buttonAction the action (to run)
     */
    public void onRightClick(ButtonAction buttonAction) {
        addAction(Action.RIGHT_CLICK, buttonAction);
    }

    /**
     * Add an action to the button for both left and right clicks
     *
     * @param buttonAction the action (to run)
     */
    public void onClick(ButtonAction buttonAction) {
        onLeftClick(buttonAction);
        onRightClick(buttonAction);
    }

    @Override
    public boolean tick() {
        // no-op
        return false;
    }

    @Nullable
    @Override
    public ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        return itemStack;
    }

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        actions.getOrDefault(action, List.of()).forEach(buttonAction -> buttonAction.invoke(viewer));
    }
}
