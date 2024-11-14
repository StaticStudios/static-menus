package net.staticstudios.menus.button;

import net.staticstudios.menus.action.ButtonAction;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * Represents a button which cannot be modified after its creation
 */
public class SimpleButton implements Button {
    private final ItemStack itemStack;
    private final Map<Action, List<ButtonAction>> actions;

    /**
     * Create a new simple button
     * @param source the source item stack
     * @param actions the actions
     */
    protected SimpleButton(ItemStack source, Map<Action, List<ButtonAction>> actions) {
        this.itemStack = new ItemStack(source);
        this.actions = actions;

        this.itemStack.editMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP));
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
