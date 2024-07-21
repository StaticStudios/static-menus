package net.staticstudios.menus.button;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.menu.PagedMenu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PreviousPageButton implements Button {
    protected PreviousPageButton() {
        ButtonRegistry.register("next", "page", this);
    }

    @Nullable
    @Override
    public ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        ItemStack item = new ItemStack(Material.SPECTRAL_ARROW);
        item.editMeta(meta -> {
            meta.displayName(StaticMenus.getMiniMessage().deserialize("<yellow>Previous page"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        });

        return item;
    }

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        if (menu instanceof PagedMenu pagedMenu) {
            pagedMenu.previousPage();
        }
    }
}
