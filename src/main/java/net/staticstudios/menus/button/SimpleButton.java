package net.staticstudios.menus.button;

import net.staticstudios.menus.action.ButtonAction;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class SimpleButton implements Button {
    private final ItemStack itemStack;
    private final Map<Action, List<ButtonAction>> actions;

    protected SimpleButton(ItemStack itemStack, Map<Action, List<ButtonAction>> actions) {
        this.itemStack = itemStack;
        this.actions = actions;
    }

    @Nullable
    @Override
    public ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        return itemStack.clone();
    }

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        actions.getOrDefault(action, List.of()).forEach(buttonAction -> buttonAction.invoke(viewer));
    }
}
