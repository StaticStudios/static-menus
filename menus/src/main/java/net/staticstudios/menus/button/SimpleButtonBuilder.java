package net.staticstudios.menus.button;

import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ButtonAction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleButtonBuilder implements Cloneable, ButtonBuilder {
    private final boolean mutable;
    private final Map<Button.Action, List<ButtonAction>> actions = new HashMap<>();
    private Component name;
    private List<Component> description = new ArrayList<>();
    private Material material = null;
    private boolean enchanted = false;
    private int amount = 1;

    protected SimpleButtonBuilder(boolean mutable) {
        this.mutable = mutable;
    }

    /**
     * Set the name of the button
     *
     * @param name the name
     * @return the builder
     */
    public SimpleButtonBuilder name(String name) {
        SimpleButtonBuilder builder = clone();
        builder.name = StaticMenus.getMiniMessage().deserialize(name);

        return builder;
    }

    /**
     * Set the name of the button
     *
     * @param name the name
     * @return the builder
     */
    public SimpleButtonBuilder name(Component name) {
        SimpleButtonBuilder builder = clone();
        builder.name = name;

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public SimpleButtonBuilder description(String... description) {
        return description(List.of(description));
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public SimpleButtonBuilder description(List<String> description) {
        SimpleButtonBuilder builder = clone();
        builder.description = description.stream().map(StaticMenus.getMiniMessage()::deserialize).toList();

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public SimpleButtonBuilder componentDescription(List<Component> description) {
        SimpleButtonBuilder builder = clone();
        builder.description = description;

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public SimpleButtonBuilder description(String description) {
        SimpleButtonBuilder builder = clone();
        builder.description.add(StaticMenus.getMiniMessage().deserialize(description));

        return builder;
    }

    /**
     * Set the material of the button
     *
     * @param material the material
     * @return the builder
     */
    public SimpleButtonBuilder material(Material material) {
        SimpleButtonBuilder builder = clone();
        builder.material = material;

        return builder;
    }

    /**
     * Set the amount of the button
     *
     * @param amount the amount
     * @return the builder
     */
    public SimpleButtonBuilder amount(int amount) {
        SimpleButtonBuilder builder = clone();
        builder.amount = amount;

        return builder;
    }

    /**
     * Set if the button should be enchanted
     *
     * @param enchanted if the button should be enchanted
     * @return the builder
     */
    public SimpleButtonBuilder enchanted(boolean enchanted) {
        SimpleButtonBuilder builder = clone();
        builder.enchanted = enchanted;

        return builder;
    }

    /**
     * Add an action to run when the button is left-clicked
     *
     * @param action the action
     * @return the builder
     */
    public SimpleButtonBuilder onLeftClick(ButtonAction action) {
        SimpleButtonBuilder builder = clone();
        List<ButtonAction> actions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.LEFT_CLICK, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Button.Action.LEFT_CLICK, actions);

        return builder;
    }

    /**
     * Add an action to run when the button is right-clicked
     *
     * @param action the action
     * @return the builder
     */
    public SimpleButtonBuilder onRightClick(ButtonAction action) {
        SimpleButtonBuilder builder = clone();
        List<ButtonAction> actions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.RIGHT_CLICK, new ArrayList<>()));
        actions.add(action);
        builder.actions.put(Button.Action.RIGHT_CLICK, actions);

        return builder;
    }

    /**
     * Add an action to run when the button is clicked
     *
     * @param action the action
     * @return the builder
     */
    public SimpleButtonBuilder onClick(ButtonAction action) {
        SimpleButtonBuilder builder = clone();
        List<ButtonAction> rightClickActions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.RIGHT_CLICK, new ArrayList<>()));
        List<ButtonAction> leftClickActions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.LEFT_CLICK, new ArrayList<>()));
        rightClickActions.add(action);
        leftClickActions.add(action);
        builder.actions.put(Button.Action.RIGHT_CLICK, rightClickActions);
        builder.actions.put(Button.Action.LEFT_CLICK, leftClickActions);

        return builder;
    }


    @Override
    public Button build() {
        if (material == null) throw new IllegalStateException("Material must be set");
        if (name == null) throw new IllegalStateException("Name must be set");

        ItemStack itemStack = new ItemStack(material);

        itemStack.editMeta(meta -> {
            meta.displayName(name);
            meta.lore(description);

            if (enchanted) {
                meta.setEnchantmentGlintOverride(true);
            }

            meta.setMaxStackSize(99);
        });

        itemStack.setAmount(amount);

        return new SimpleButton(itemStack, actions);
    }


    public SimpleButtonBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            return (SimpleButtonBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
