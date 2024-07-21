package net.staticstudios.menus.viewer;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
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
    }

    void sendMessage(Component message);

    default void sendMessage(String message) {
        sendMessage(StaticMenus.getMiniMessage().deserialize(message));
    }


    @Override
    boolean equals(Object obj);

    @Override
    int hashCode();
}
