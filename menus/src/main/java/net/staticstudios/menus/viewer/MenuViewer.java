package net.staticstudios.menus.viewer;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.menu.Menu;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface MenuViewer {
    static MenuViewer wrap(Player player) {
        return new PlayerViewer(player);
    }

    UUID getId();

    Player getPlayer();

    default void closeMenu() {
        getPlayer().closeInventory();
        StaticMenus.getHistory(this).clear();
    }

    void sendMessage(Component message);

    default void sendMessage(String message) {
        sendMessage(StaticMenus.getMiniMessage().deserialize(message));
    }

    default boolean isViewingMenu(String menuId) {
        Player player = getPlayer();
        if (player.getOpenInventory().getTopInventory().getHolder(false) instanceof Menu menu) {
            return menu.getId().equals(menuId);
        }

        return false;
    }

    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
