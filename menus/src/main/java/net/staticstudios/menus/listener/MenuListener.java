package net.staticstudios.menus.listener;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.button.EmptyButton;
import net.staticstudios.menus.menu.InteractableMenu;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class MenuListener implements Listener {

    @EventHandler
    void onButtonClick(InventoryClickEvent e) {
        UUID playerId = e.getWhoClicked().getUniqueId();
        if (e.getClickedInventory() == null) {
            return;
        }
        Menu menu = null;
        if (!(e.getClickedInventory().getHolder(false) instanceof Menu m)) {
            if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                if (e.getInventory().getHolder(false) instanceof Menu toHolder) {
                    if (toHolder instanceof InteractableMenu interactableMenu && interactableMenu.isInteractable(e.getCurrentItem())) {
                        menu = toHolder;

                        if (e.getCurrentItem() == null || e.getCurrentItem().isEmpty()) {
                            e.setCancelled(true);
                            return;
                        }

                        int slot = -1;
                        for (int i = 0; i < e.getInventory().getSize(); i++) {
                            if (!interactableMenu.isInteractable(i)) {
                                continue;
                            }
                            ItemStack inSlot = e.getInventory().getItem(i);
                            if (inSlot == null || inSlot.getType().isEmpty()) {
                                slot = i;
                                break;
                            }

                            int maxCount = inSlot.getMaxStackSize();
                            if (inSlot.isSimilar(e.getCurrentItem()) && inSlot.getAmount() < maxCount) {
                                slot = i;
                                break;
                            }
                        }

                        e.setCancelled(true);
                        if (slot == -1) {
                            return;
                        }

                        ItemStack inSlot = e.getInventory().getItem(slot);
                        if (inSlot == null || inSlot.getType().isEmpty()) {
                            e.getInventory().setItem(slot, e.getCurrentItem());
                            e.getCurrentItem().setAmount(0);
                            return;
                        }

                        int amountToMove = Math.min(e.getCurrentItem().getAmount(), inSlot.getMaxStackSize() - inSlot.getAmount());

                        inSlot.setAmount(inSlot.getAmount() + amountToMove);
                        e.getInventory().setItem(slot, inSlot);
                        e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() - amountToMove);

                        Bukkit.getScheduler().runTaskLater(StaticMenus.getPlugin(), () -> {
                            Player player = Bukkit.getPlayer(playerId);
                            if (player != null && interactableMenu == player.getOpenInventory().getTopInventory().getHolder(false)) {
                                interactableMenu.callUpdateActions();
                            }
                        }, 1);
                        return;

                    } else {
                        e.setCancelled(true);
                    }
                }
            }
            return;
        } else {
            menu = m;
        }
        if (menu instanceof InteractableMenu interactableMenu && interactableMenu.isInteractable(e.getSlot())) {
            if (!e.getCursor().isEmpty() && !interactableMenu.isInteractable(e.getCursor())) {
                e.setCancelled(true);
            } else {
                Bukkit.getScheduler().runTaskLater(StaticMenus.getPlugin(), () -> {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null && interactableMenu == player.getOpenInventory().getTopInventory().getHolder(false)) {
                        interactableMenu.callUpdateActions();
                    }
                }, 1);
            }
            return;
        }
        e.setCancelled(true);
        Button button = menu.getButton(e.getSlot());

        if (button instanceof EmptyButton) {
            return;
        }

        MenuViewer viewer = StaticMenus.getViewer((Player) e.getWhoClicked());
        Button.Action action = e.isRightClick() ? Button.Action.RIGHT_CLICK : Button.Action.LEFT_CLICK;
        button.invokeActions(action, menu, viewer);
    }

    @EventHandler
    void onDrag(InventoryDragEvent e) {
        UUID playerId = e.getWhoClicked().getUniqueId();
        if (e.getInventory().getHolder(false) instanceof Menu m) {
            if (m instanceof InteractableMenu interactableMenu) {
                for (int slot : e.getInventorySlots()) {
                    if (!interactableMenu.isInteractable(slot) || !(interactableMenu.isInteractable(e.getNewItems().get(slot)))) {
                        e.setCancelled(true);
                        return;
                    }
                }
                Bukkit.getScheduler().runTaskLater(StaticMenus.getPlugin(), () -> {
                    Player player = Bukkit.getPlayer(playerId);
                    if (player != null && interactableMenu == player.getOpenInventory().getTopInventory().getHolder(false)) {
                        interactableMenu.callUpdateActions();
                    }
                }, 1);
            } else {
                e.setCancelled(true);
            }
        }

        //todo: this is a temporary fix, we need to implement a better solution
    }

    @EventHandler
    void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder(false) instanceof Menu menu)) {
            return;
        }
        if (!e.getReason().equals(InventoryCloseEvent.Reason.OPEN_NEW) && !e.getReason().equals(InventoryCloseEvent.Reason.PLUGIN)) {
            MenuViewer viewer = StaticMenus.getViewer((Player) e.getPlayer());
            StaticMenus.getHistory(viewer).clear();
        }

        menu.runActions(Menu.Action.CLOSE, StaticMenus.getViewer((Player) e.getPlayer()));
    }

    @EventHandler
    void onOpen(InventoryOpenEvent e) {
        if (e.getInventory().getHolder(false) instanceof Menu) {
            return;
        }
        MenuViewer viewer = StaticMenus.getViewer((Player) e.getPlayer());
        StaticMenus.getHistory(viewer).clear();
    }
}
