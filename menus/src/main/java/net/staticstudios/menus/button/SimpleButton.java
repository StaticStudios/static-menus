package net.staticstudios.menus.button;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
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
    private final List<ButtonUpdateAction<SimpleButton>> updateActions;

    /**
     * Create a new simple button
     *
     * @param source  the source item stack
     * @param actions the actions
     */
    protected SimpleButton(ItemStack source, Map<Action, List<ButtonAction>> actions, @NotNull List<ButtonUpdateAction<SimpleButton>> updateActions) {
        this.itemStack = new ItemStack(source);
        this.actions = actions;
        this.updateActions = updateActions;

        this.itemStack.editMeta(meta -> meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP));
    }

    @Override
    public void setName(Component name) {
        itemStack.setData(DataComponentTypes.CUSTOM_NAME, name);
    }

    @Override
    public void componentDescription(List<Component> description) {
        itemStack.setData(DataComponentTypes.LORE, ItemLore.lore(description));
    }

    @Override
    public boolean tick() {
        boolean update = false;
        for (ButtonUpdateAction<SimpleButton> updateAction : updateActions) {
            if (updateAction.tick(this)) {
                update = true;
            }
        }
        return update;
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
