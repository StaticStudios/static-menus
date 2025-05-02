package net.staticstudios.menus.button;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import net.kyori.adventure.text.Component;
import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.action.ButtonAction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;
import java.util.function.Consumer;

public class PlayerSkullButtonBuilder implements Cloneable, ButtonBuilder {
    private final boolean mutable;
    private final Map<Button.Action, List<ButtonAction>> actions = new HashMap<>();
    private final List<ButtonUpdateAction<SimpleButton>> updateActions = new ArrayList<>();
    private Component name;
    private List<Component> description = new ArrayList<>();
    private boolean enchanted = false;
    private int amount = 1;
    private PlayerProfile playerProfile;

    protected PlayerSkullButtonBuilder(boolean mutable) {
        this.mutable = mutable;
    }

    /**
     * Set the name of the button
     *
     * @param name the name
     * @return the builder
     */
    public PlayerSkullButtonBuilder name(String name) {
        PlayerSkullButtonBuilder builder = clone();
        builder.name = StaticMenus.getMiniMessage().deserialize(name);

        return builder;
    }

    /**
     * Set the name of the button
     *
     * @param name the name
     * @return the builder
     */
    public PlayerSkullButtonBuilder name(Component name) {
        PlayerSkullButtonBuilder builder = clone();
        builder.name = name;

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public PlayerSkullButtonBuilder description(List<String> description) {
        PlayerSkullButtonBuilder builder = clone();
        builder.description = description.stream().map(StaticMenus.getMiniMessage()::deserialize).toList();

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public PlayerSkullButtonBuilder description(String... description) {
        return description(List.of(description));
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public PlayerSkullButtonBuilder description(String description) {
        PlayerSkullButtonBuilder builder = clone();
        builder.description.add(StaticMenus.getMiniMessage().deserialize(description));

        return builder;
    }

    /**
     * Set the description of the button
     *
     * @param description the description
     * @return the builder
     */
    public PlayerSkullButtonBuilder componentDescription(List<Component> description) {
        PlayerSkullButtonBuilder builder = clone();
        builder.description = description;

        return builder;
    }


    /**
     * Set the amount of the button
     *
     * @param amount the amount
     * @return the builder
     */
    public PlayerSkullButtonBuilder amount(int amount) {
        PlayerSkullButtonBuilder builder = clone();
        builder.amount = amount;

        return builder;
    }

    /**
     * Set if the button should be enchanted
     *
     * @param enchanted if the button should be enchanted
     * @return the builder
     */
    public PlayerSkullButtonBuilder enchanted(boolean enchanted) {
        PlayerSkullButtonBuilder builder = clone();
        builder.enchanted = enchanted;

        return builder;
    }

    /**
     * Set the player profile of the button
     *
     * @param playerProfile the player profile
     * @return the builder
     */
    public PlayerSkullButtonBuilder playerProfile(PlayerProfile playerProfile) {
        PlayerSkullButtonBuilder builder = clone();
        builder.playerProfile = playerProfile;

        return builder;
    }

    /**
     * Set the player profile from a player for the button
     *
     * @param player the player
     * @return the builder
     */
    public PlayerSkullButtonBuilder player(Player player) {
        PlayerSkullButtonBuilder builder = clone();
        builder.playerProfile = player.getPlayerProfile();

        return builder;
    }

    /**
     * Set the player profile from a player id for the button
     *
     * @param playerId the player id
     * @return the builder
     */
    public PlayerSkullButtonBuilder playerId(UUID playerId) {
        PlayerSkullButtonBuilder builder = clone();
        builder.playerProfile = Bukkit.createProfile(playerId);

        return builder;
    }

    /**
     * Set the texture of the player skull
     *
     * @param base64Texture the texture, encoded in base64
     * @return the builder
     */
    public PlayerSkullButtonBuilder texture(String base64Texture) {
        PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID());
        profile.setProperty(new ProfileProperty("textures", base64Texture));

        PlayerSkullButtonBuilder builder = clone();
        builder.playerProfile = profile;

        return builder;
    }

    /**
     * Add an action to run when the button is left-clicked
     *
     * @param action the action
     * @return the builder
     */
    public PlayerSkullButtonBuilder onLeftClick(ButtonAction action) {
        PlayerSkullButtonBuilder builder = clone();
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
    public PlayerSkullButtonBuilder onRightClick(ButtonAction action) {
        PlayerSkullButtonBuilder builder = clone();
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
    public PlayerSkullButtonBuilder onClick(ButtonAction action) {
        PlayerSkullButtonBuilder builder = clone();
        List<ButtonAction> rightClickActions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.RIGHT_CLICK, new ArrayList<>()));
        List<ButtonAction> leftClickActions = new ArrayList<>(builder.actions.getOrDefault(Button.Action.LEFT_CLICK, new ArrayList<>()));
        rightClickActions.add(action);
        leftClickActions.add(action);
        builder.actions.put(Button.Action.RIGHT_CLICK, rightClickActions);
        builder.actions.put(Button.Action.LEFT_CLICK, leftClickActions);

        return builder;
    }


    public ButtonBuilder update(int tickInterval, Consumer<SimpleButton> action) {
        PlayerSkullButtonBuilder builder = clone();
        updateActions.add(new ButtonUpdateAction<>(tickInterval, action));

        return builder;
    }

    @Override
    public Button build() {
        if (playerProfile == null) throw new IllegalStateException("Player profile must be set");
        if (name == null) throw new IllegalStateException("Name must be set");

        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD, amount);

        itemStack.editMeta(meta -> {
            SkullMeta skullMeta = (SkullMeta) meta;
            skullMeta.setPlayerProfile(playerProfile);

            meta.displayName(name);
            meta.lore(description);

            if (enchanted) {
                meta.setEnchantmentGlintOverride(true);
            }
        });

        return new SimpleButton(itemStack, actions, updateActions);
    }


    public PlayerSkullButtonBuilder clone() {
        if (mutable) {
            return this;
        }
        try {
            return (PlayerSkullButtonBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}
