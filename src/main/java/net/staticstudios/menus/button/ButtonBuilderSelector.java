package net.staticstudios.menus.button;

public class ButtonBuilderSelector {
    protected ButtonBuilderSelector() {
    }

    /**
     * Get a {@link SimpleButtonBuilder} builder
     *
     * @return The simple button builder
     */
    public SimpleButtonBuilder simple() {
        return new SimpleButtonBuilder(false);
    }

    /**
     * Get a {@link PlayerSkullButtonBuilder} builder
     *
     * @return The player skull button builder
     */
    public PlayerSkullButtonBuilder skull() {
        return new PlayerSkullButtonBuilder(false);
    }

    /**
     * Get a mutable {@link SimpleButtonBuilder} builder.
     * This is primarily for internal use.
     *
     * @return The builder
     */
    public SimpleButtonBuilder mutableSimple() {
        return new SimpleButtonBuilder(true);
    }

    /**
     * Get a mutable {@link PlayerSkullButtonBuilder} builder.
     * This is primarily for internal use.
     *
     * @return The builder
     */
    public PlayerSkullButtonBuilder mutableSkull() {
        return new PlayerSkullButtonBuilder(true);
    }
}
