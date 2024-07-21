package net.staticstudios.menus;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.staticstudios.menus.history.MenuHistory;
import net.staticstudios.menus.listener.MenuListener;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class StaticMenus {
    private static final Map<MenuViewer, MenuHistory> PLAYER_HISTORY = new HashMap<>();
    private static Function<Player, MenuViewer> getViewerFunction;
    private static JavaPlugin plugin;
    private static MiniMessage miniMessage;

    public static void enable(JavaPlugin plugin, MiniMessage miniMessage, Function<Player, MenuViewer> getViewerFunction) {
        StaticMenus.plugin = plugin;
        StaticMenus.getViewerFunction = getViewerFunction;
        StaticMenus.miniMessage = miniMessage;

        Bukkit.getPluginManager().registerEvents(new MenuListener(), plugin);
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static MenuHistory getHistory(MenuViewer viewer) {
        return PLAYER_HISTORY.computeIfAbsent(viewer, v -> new MenuHistory());
    }

    public static MiniMessage getMiniMessage() {
        return miniMessage;
    }

    public static MenuViewer getViewer(Player player) {
        return getViewerFunction.apply(player);
    }
}
