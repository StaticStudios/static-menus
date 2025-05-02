package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class EmptyButton implements Button {
    @Override
    public @Nullable ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        return null;
    }

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        // Do nothing
    }

    @Override
    public void setName(Component name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void componentDescription(List<Component> description) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean tick() {
        // no-op
        return false;
    }
}
