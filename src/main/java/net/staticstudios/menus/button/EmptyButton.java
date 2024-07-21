package net.staticstudios.menus.button;

import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class EmptyButton implements Button {
    @Override
    public @Nullable ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        return null;
    }

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        // Do nothing
    }
}
