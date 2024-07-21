package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public interface Button {
    Button BACK = new BackButton();
    Button NEXT_PAGE = new NextPageButton();
    Button PREVIOUS_PAGE = new PreviousPageButton();
    Button EMPTY = new EmptyButton();

    /**
     * Get a builder selector
     *
     * @return the builder selector
     */
    static ButtonBuilderSelector builder() {
        return new ButtonBuilderSelector();
    }

    /**
     * Create a {@link MutableButton} from an {@link ItemStack}
     *
     * @param itemStack the item stack to use
     * @return the created button
     */
    static Button fromItemStack(ItemStack itemStack) { //todo: this should return a mutable button
        return new SimpleButton(itemStack, Map.of());
    }

    /**
     * Get the item representation of the button.
     * This is the item that will be displayed in the menu.
     *
     * @param viewer the viewer
     * @param menu   the menu
     * @return the item stack
     */
    @Nullable
    ItemStack getItemRepresentation(MenuViewer viewer, Menu menu);

    /**
     * Invoke the actions of the button
     *
     * @param action the action (type)
     * @param menu   the menu
     * @param viewer the viewer
     */
    void invokeActions(@NotNull Action action, @NotNull Menu menu, @NotNull MenuViewer viewer);

    enum Action {
        LEFT_CLICK,
        RIGHT_CLICK
    }

    class Placeholder {
        //The ids reflect named text colors for easy mappings of color -> button
        public static final Button RED = builder().simple().material(Material.RED_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "red");
        public static final Button GREEN = builder().simple().material(Material.GREEN_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "dark_green");
        public static final Button BLUE = builder().simple().material(Material.BLUE_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "blue");
        public static final Button YELLOW = builder().simple().material(Material.YELLOW_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "yellow");
        public static final Button PURPLE = builder().simple().material(Material.PURPLE_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "dark_purple");
        public static final Button ORANGE = builder().simple().material(Material.ORANGE_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "gold");
        public static final Button LIGHT_BLUE = builder().simple().material(Material.LIGHT_BLUE_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "aqua");
        public static final Button LIME = builder().simple().material(Material.LIME_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "green");
        public static final Button CYAN = builder().simple().material(Material.CYAN_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "dark_aqua");
        public static final Button MAGENTA = builder().simple().material(Material.MAGENTA_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "light_purple");
        public static final Button LIGHT_GRAY = builder().simple().material(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "gray");
        public static final Button GRAY = builder().simple().material(Material.GRAY_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "dark_gray");
        public static final Button BROWN = builder().simple().material(Material.BROWN_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "brown");
        public static final Button WHITE = builder().simple().material(Material.WHITE_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "white");
        public static final Button BLACK = builder().simple().material(Material.BLACK_STAINED_GLASS_PANE).name(Component.empty()).buildAndRegister("placeholder", "black");
    }
}
