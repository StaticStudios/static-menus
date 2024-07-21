package net.staticstudios.menus.options;

import net.staticstudios.menus.button.Button;
import org.jetbrains.annotations.NotNull;

public class MenuOptions implements Cloneable {
    private @NotNull Button defaultPlaceholder = Button.EMPTY;

    public MenuOptions() {
    }

    public MenuOptions defaultPlaceholder(@NotNull Button defaultPlaceholder) {
        MenuOptions opts = clone();
        opts.defaultPlaceholder = defaultPlaceholder;
        return opts;
    }

    public @NotNull Button defaultPlaceholder() {
        return defaultPlaceholder;
    }

    public MenuOptions clone() {
        try {
            return (MenuOptions) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
