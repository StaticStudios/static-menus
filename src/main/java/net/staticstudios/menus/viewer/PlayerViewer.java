package net.staticstudios.menus.viewer;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerViewer implements MenuViewer {
    private final Player player;

    protected PlayerViewer(Player player) {
        this.player = player;
    }

    @Override
    public UUID getId() {
        return player.getUniqueId();
    }

    @Override
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void sendMessage(Component message) {
        player.sendMessage(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlayerViewer that = (PlayerViewer) obj;
        return player.equals(that.player);
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }
}
