package net.staticstudios.menus.options;

import net.staticstudios.menus.action.ViewerAction;
import net.staticstudios.menus.button.Button;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MenuOptions implements Cloneable {
    private @NotNull Button defaultPlaceholder = Button.EMPTY;
    private ViewerAction onComeBack = null;

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

    public MenuOptions onComeBack(ViewerAction onComeBack) {
        MenuOptions opts = clone();
        opts.onComeBack = onComeBack;
        return opts;
    }

    public @Nullable ViewerAction onComeBack() {
        return onComeBack;
    }

    public MenuOptions clone() {
        try {
            return (MenuOptions) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

}
