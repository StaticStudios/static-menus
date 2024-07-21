package net.staticstudios.menus.button;

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

}
