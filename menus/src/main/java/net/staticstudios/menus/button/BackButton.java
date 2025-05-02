package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ButtonAction;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BackButton implements Button {
    protected BackButton() {
        ButtonRegistry.register("back", "button", this);
    }

    @Override
    public ItemStack getItemRepresentation(MenuViewer viewer, Menu menu) {
        if (StaticMenus.getHistory(viewer).size() <= 1) {
            return menu.getOptions().defaultPlaceholder().getItemRepresentation(viewer, menu);
        }
        ItemStack item = new ItemStack(Material.ARROW);
        item.editMeta(meta -> {
            meta.displayName(StaticMenus.getMiniMessage().deserialize("<red>Go Back"));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        });

        return item;
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

    @Override
    public void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer) {
        ButtonAction.goBack().invoke(viewer);
    }
}
