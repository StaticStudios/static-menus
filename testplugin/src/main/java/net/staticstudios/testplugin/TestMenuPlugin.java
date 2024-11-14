package net.staticstudios.testplugin;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

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
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            getSLF4JLogger().warn("TestCommand not found. Did you forget to copy TestCommandTemplate.java and rename it to TestCommand.java?");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            getSLF4JLogger().error("Error while loading TestCommand");
            throw new RuntimeException(e);
        }

        // Create the data folder if it doesn't exist
        getDataFolder().mkdirs();
        StaticMenus.enable(this, MiniMessage.miniMessage(), MenuViewer::wrap);
    }
}