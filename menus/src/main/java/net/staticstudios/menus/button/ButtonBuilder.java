package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.action.ButtonAction;

import java.util.List;

public interface ButtonBuilder {

    /**
     * Build the button
     *
     * @return The button
     */
    Button build();

    /**
     * Build the button and register it to the {@link ButtonRegistry}
     *
     * @param namespace The namespace to register the button in
     * @param id        The id to register the button with
     * @return The button
     */
    default Button buildAndRegister(String namespace, String id) {
        Button button = build();
        ButtonRegistry.register(namespace, id, button);
        return button;
    }

    ButtonBuilder name(String name);

    ButtonBuilder name(Component name);

    ButtonBuilder description(String... description);

    ButtonBuilder description(List<String> description);

    ButtonBuilder componentDescription(List<Component> description);

    ButtonBuilder description(String description);

    ButtonBuilder amount(int amount);

    ButtonBuilder enchanted(boolean enchanted);

    ButtonBuilder onLeftClick(ButtonAction action);

    ButtonBuilder onRightClick(ButtonAction action);

    ButtonBuilder onClick(ButtonAction action);


}
