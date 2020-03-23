package me.anfanik.steda.api.menu;

import me.anfanik.steda.api.menu.button.ClickCallback;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * @author Anfanik
 * Date: 18/09/2019
 */

public class MenuService implements Listener {

    private static MenuService instance;

    public static MenuService get() {
        return instance;
    }

    public MenuService(Plugin plugin, PluginManager pluginManager) {
        instance = this;
        pluginManager.registerEvents(this, plugin);
    }

    public Menu<MenuSession> confirmationMenu(ClickCallback<?> confirmationCallback) {
        return confirmationMenu(confirmationCallback, (ignored1, ignored2, ignored3) -> {});
    }

    public Menu<MenuSession> confirmationMenu(ClickCallback<?> confirmationCallback, ClickCallback<?> cancelCallback) {
        Menu<MenuSession> menu = new Menu<>(54);
        menu.setTitleGenerator(session -> "Подтверждение");
        menu.setFillingStrategy(session -> {
            ClickCallback<?> baseCallback = (player, clickType, slot) -> player.closeInventory();
            ItemStack confirmationItem = ItemBuilder.fromMaterial(Material.STAINED_GLASS_PANE, 5)
                    .setName("&aПодтвердить")
                    .build();
            ItemStack cancelItem = ItemBuilder.fromMaterial(Material.STAINED_GLASS_PANE, 14)
                    .setName("&cОтменить")
                    .build();

            return new FillingStrategy.ResultBuilder()
                    .addButton(new MenuButton(confirmationItem, baseCallback, confirmationCallback), 11, 12, 20, 21)
                    .addButton(new MenuButton(cancelItem, baseCallback, cancelCallback), 32, 33, 41, 42)
                    .build();
        });
        return menu;
    }

    @EventHandler(ignoreCancelled = true)
    public void handleClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && Menu.isMenu(inventory)) {
            MenuSession session = Menu.getSession(inventory);
            Menu<?> menu = session.getMenu();
            boolean cancelled = true;
            try {
                cancelled = menu.processClick(session,
                        event.getClick(), event.getSlot());
            } catch (Exception exception) {
                exception.printStackTrace();
            } finally {
                event.setCancelled(cancelled);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void handlePlayerInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (Menu.isMenu(topInventory) && inventory instanceof PlayerInventory) {
            MenuSession session = Menu.getSession(topInventory);
            Menu<?> menu = session.getMenu();
            event.setCancelled(menu.isLockUserInventory());
        }
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void handleOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && Menu.isMenu(inventory)) {
            MenuSession session = Menu.getSession(inventory);
            Menu menu = session.getMenu();
            menu.processOpen(session);
        }
    }

    @SuppressWarnings("unchecked")
    @EventHandler
    public void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && Menu.isMenu(inventory)) {
            MenuSession session = Menu.getSession(inventory);
            Menu menu = session.getMenu();
            menu.processClose(session);
        }
    }

}
