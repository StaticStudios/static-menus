package net.staticstudios.testplugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class TestMenuPlugin extends JavaPlugin {
    private static TestMenuPlugin instance;

    public static TestMenuPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        try {
            Class<?> testCommandClass = Class.forName(this.getClass().getPackageName() + ".TestCommand");
            Objects.requireNonNull(getCommand("test")).setExecutor((CommandExecutor) testCommandClass.getDeclaredConstructor().newInstance());
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            // No TestCommand class found
        }

        // Create the data folder if it doesn't exist
        getDataFolder().mkdirs();
        StaticMenus.enable(this, MiniMessage.miniMessage(), MenuViewer::wrap);

        // Load some menus to test with
        loadMenus();
    }

    /**
     * Load all the menus in the menus resource directory
     */
    private void loadMenus() {
        getSLF4JLogger().info("Loading menus from resource directory...");
        // Load menus from the menus directory
        FileUtils.getResourceDirectory("menus").forEach(path -> {
            getSLF4JLogger().info("Loading menu: {}", path);
            Reader reader = FileUtils.getResourceContents(path);
            YamlConfiguration config = YamlConfiguration.loadConfiguration(reader);
            StaticMenus.getParser().parseMenu(path, config);
        });
    }
}
