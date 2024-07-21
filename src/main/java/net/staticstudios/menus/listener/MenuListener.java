package net.staticstudios.menus.listener;

import net.staticstudios.menus.StaticMenus;
import net.staticstudios.menus.button.Button;
import net.staticstudios.menus.button.EmptyButton;
import net.staticstudios.menus.menu.Menu;
import net.staticstudios.menus.viewer.MenuViewer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class MenuListener implements Listener {

    @EventHandler
    void onButtonClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) {
            return;
        }
        Menu menu = null;
        if (!(e.getClickedInventory().getHolder() instanceof Menu m)) {
            if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                if (e.getInventory().getHolder() instanceof Menu) {
                    e.setCancelled(true);
                }
            }
            return;
        } else {
            menu = m;
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
        if (e.getInventory().getHolder() instanceof Menu) {
            e.setCancelled(true);
        }

        //todo: this is a temporary fix, we need to implement a better solution
    }

    @EventHandler
    void onClose(InventoryCloseEvent e) {
        if (!(e.getInventory().getHolder() instanceof Menu menu)) {
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
        if (e.getInventory().getHolder() instanceof Menu) {
            return;
        }
        MenuViewer viewer = StaticMenus.getViewer((Player) e.getPlayer());
        StaticMenus.getHistory(viewer).clear();
    }
}
