package net.staticstudios.menus.parser;

import net.staticstudios.menus.action.ButtonAction;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.button.PlayerSkullButtonBuilder;
import net.staticstudios.menus.button.SimpleButtonBuilder;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.slf4j.Logger;

import java.util.*;

public class ButtonParser {
    private static final Set<String> VALID_ACTIONS = Set.of("close-menu", "go-back", "command", "send-message");

    private final Logger logger;

    public ButtonParser(Logger logger) {
        this.logger = logger;
    }

    public boolean validateButton(String id, ConfigurationSection config) {
        ConfigurationSection itemRepresentation = config.getConfigurationSection("item");

        if (itemRepresentation == null) {
            logger.warn("Invalid item representation! Def value not found in layout. {}.item", id);
            return false;
        }

        int amount = config.getInt("item.amount", 1);
        String itemName = config.getString("item.name");
        String itemMaterial = config.getString("item.material");

        if (itemName == null) {
            logger.warn("Invalid item name! {}.item.name", id);
            return false;
        }

        if (itemMaterial == null) {
            String skullId = config.getString("item.head-id");
            if (skullId == null) {
                logger.warn("Invalid item material/head-id! {}.item.material, {}.item.head-id", id, id);
                return false;
            }
        }

        if (amount < 0) {
            logger.warn("Invalid item amount! {}.item.amount", id);
            return false;
        }

        List<String> actionIds = config.getStringList("actions");
        actionIds.addAll(config.getMapList("actions").stream().map(map -> (String) map.keySet().iterator().next()).toList());

        if (!VALID_ACTIONS.containsAll(actionIds)) {
            logger.warn("Invalid action found, action does not exist! (Valid actions: {}, got: {}) {}.actions", String.join(", ", VALID_ACTIONS), String.join(", ", actionIds), id);
            return false;
        }

        return true;
    }

    public Button parseButton(ConfigurationSection config) {
        List<ButtonAction> actions = new ArrayList<>();

        actions.addAll(config.getMapList("actions")
                .stream()
                .map(actionMap -> {
                    String actionId = (String) actionMap.keySet().iterator().next();

                    return switch (actionId) {
                        case "close-menu" -> ButtonAction.closeMenu();
                        case "go-back" -> ButtonAction.goBack();
                        case "command" -> {
                            String command = (String) actionMap.get(actionId);

                            if (command == null) {
                                throw new IllegalArgumentException("Command action requires a command to be run!");
                            }

                            yield ButtonAction.command(command);
                        }
                        case "send-message" -> {
                            String message = (String) actionMap.get(actionId);

                            if (message == null) {
                                throw new IllegalArgumentException("Send message action requires a message to send!");
                            }

                            yield ButtonAction.sendMessage(message);
                        }
                        default -> throw new UnsupportedOperationException("Action not implemented: " + actionId);
                    };
                })
                .toList()
        );

        //Since we can have go-back & close-menu actions without args, we account for that here
        actions.addAll(
                config.getStringList("actions").stream().map(actionId -> switch (actionId) {
                    case "close-menu" -> ButtonAction.closeMenu();
                    case "go-back" -> ButtonAction.goBack();
                    case "command" ->
                            throw new UnsupportedOperationException("Cannot call a command action without args!");
                    case "send-message" ->
                            throw new UnsupportedOperationException("Cannot call a send message action without args!");
                    default -> throw new UnsupportedOperationException("Action not implemented: " + actionId);
                }).toList()
        );

        if (config.contains("item.head-id")) {
            PlayerSkullButtonBuilder builder = Button.builder().mutableSkull()
                    .name(config.getString("item.name"))
                    .description(config.getStringList("item.lore"))
                    .amount(config.getInt("item.amount", 1))
                    .enchanted(config.getBoolean("item.enchanted"))
                    .playerId(UUID.fromString(Objects.requireNonNull(config.getString("item.head-id"))));

            for (ButtonAction action : actions) {
                builder.onClick(action);
            }

            return builder.build();
        } else {
            SimpleButtonBuilder builder = Button.builder().mutableSimple()
                    .name(config.getString("item.name"))
                    .description(config.getStringList("item.lore"))
                    .amount(config.getInt("item.amount", 1))
                    .material(Material.valueOf(config.getString("item.material")))
                    .enchanted(config.getBoolean("item.enchanted"));

            for (ButtonAction action : actions) {
                builder.onClick(action);
            }

            return builder.build();
        }
    }
}
