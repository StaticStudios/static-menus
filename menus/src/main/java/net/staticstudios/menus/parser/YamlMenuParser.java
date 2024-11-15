package net.staticstudios.menus.parser;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.button.ButtonRegistry;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.menu.SimpleMenuBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.slf4j.Logger;

import java.util.List;

public class YamlMenuParser {
    private final ButtonParser buttonParser = new ButtonParser(getLogger());

    public YamlMenuParser() {
        // Ensure that the placeholders are registered
        Button initPlaceholders = Button.Placeholder.BLACK;
    }

    /**
     * Parse a menu from a configuration section, and register it with the registry.
     *
     * @param id     The id of the menu
     * @param config The configuration section to parse
     */
    public void parseMenu(String id, ConfigurationSection config) {
        getLogger().debug("Validating menu file: {}", id);

        if (!validateMenuConfig(config)) {
            getLogger().warn("Menu file is invalid. Id: {}", id);
            return;
        }
        getLogger().debug("Menu file is valid. Id: {}", id);

        String title = config.getString("title");
        String placeholder = config.getString("fill-with");
        List<String> layoutStrings = config.getStringList("layout");
        int rows = layoutStrings.size();

        SimpleMenuBuilder builder = Menu.builder().mutableSimple()
                .id(id)
                .title(title)
                .size(rows * 9)
                .defaultPlaceholder(ButtonRegistry.getButton(placeholder));

        ConfigurationSection defs = config.getConfigurationSection("defs");

        if (defs != null) {
            for (int row = 0; row < rows; row++) {
                String layoutString = layoutStrings.get(row);
                for (int column = 0; column < 9; column++) {
                    int index = row * 9 + column;
                    String character = layoutString.charAt(column) + "";

                    if (defs.isString(character)) {
                        builder.setButton(index, ButtonRegistry.getButton(defs.getString(character)));
                        continue;
                    }

                    ConfigurationSection buttonConfig = defs.getConfigurationSection(character);
                    if (buttonConfig == null) {
                        continue;
                    }

                    Button button = buttonParser.parseButton(buttonConfig);
                    builder.setButton(index, button);
                }
            }
        }

        StaticMenus.getRegistry().register(id, builder);
    }

    private boolean validateMenuConfig(ConfigurationSection config) {
        String title = config.getString("title");

        if (title == null) {
            getLogger().warn("Invalid title!");
            return false;
        }

        //Validate the layout
        List<String> layout = config.getStringList("layout");

        if (layout.isEmpty() || layout.size() > 6) {
            getLogger().warn("Invalid layout!");
            return false;
        }

        for (String row : layout) {
            if (row.length() != 9) {
                getLogger().warn("Invalid row length! Each row should be exactly 9 characters long.");
                return false;
            }
        }

        List<Character> charactersInLayout = layout.stream()
                .flatMap(row -> row.chars().mapToObj(c -> (char) c))
                .toList();

        //Validate the defs
        ConfigurationSection defs = config.getConfigurationSection("defs");

        if (defs == null) {
            getLogger().warn("No defs found. Menu is valid, but you should probably add its defs.");
            return true;
        }

        for (String key : defs.getKeys(false)) {
            if (key == null || key.length() != 1) {
                getLogger().warn("Invalid def value! Each def should be exactly 1 character long.");
                return false;
            }

            if (!charactersInLayout.contains(key.charAt(0))) {
                getLogger().warn("Invalid def value! Def value not found in layout. defs." + key);
                return false;
            }

            if (defs.isString(key)) {
                //We're going to assume its valid since we are most likely referencing a custom button
                continue;
            }

            ConfigurationSection buttonConfig = defs.getConfigurationSection(key);
            if (buttonConfig == null) {
                getLogger().warn("Invalid button config! Def value not found in layout. defs.{}", key);
                return false;
            }

            if (!buttonParser.validateButton(key, buttonConfig)) {
                return false;
            }

        }
        return true;
    }

    private Logger getLogger() {
        return StaticMenus.getPlugin().getSLF4JLogger();
    }
}
