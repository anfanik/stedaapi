package me.anfanik.steda.api.menu;

import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.menu.content.exact.PatternContentProvider;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class Menus implements Listener {

    public static final Menu<ConfirmationMenuSession> CONFIRMATION_MENU = Menu.builder(ConfirmationMenuSession.class)
            .title("Подтверждение")
            .chestRows(6)
            .contentProvider(PatternContentProvider.builder(ConfirmationMenuSession.class)
                    .matrix("---------",
                            "--yy-----",
                            "--yy-----",
                            "-----nn--",
                            "-----nn--",
                            "---------")
                    .staticItem('y',
                            ItemBuilder.fromMaterial(Material.STAINED_GLASS_PANE, 5)
                                .setName("&aПодтвердить"),
                            MenuClickHandler.<ConfirmationMenuSession>session(session -> session.getConfirm().accept(session.getPlayer()))
                    )
                    .staticItem('n',
                            ItemBuilder.fromMaterial(Material.STAINED_GLASS_PANE, 14)
                                    .setName("&cОтклонить"),
                            MenuClickHandler.<ConfirmationMenuSession>session(session -> session.getCancel().accept(session.getPlayer()))
                    )
                    .build())
            .onClose(session -> session.getCancel().accept(session.getPlayer()))
            .build();

    private static boolean initialized = false;

    public static void openConfirmationMenu(Player player, Consumer<Player> confirm) {
        CONFIRMATION_MENU.open(new ConfirmationMenuSession(player, confirm));
    }

    public static void openConfirmationMenu(Player player, Consumer<Player> confirm, Consumer<Player> cancel) {
        CONFIRMATION_MENU.open(new ConfirmationMenuSession(player, confirm, cancel));
    }

    public static boolean isMenu(Inventory inventory) {
        return inventory.getHolder() instanceof MenuSession;
    }

    public static MenuSession getSession(Inventory inventory) {
        if (isMenu(inventory)) {
            return (MenuSession) inventory.getHolder();
        }
        throw new IllegalArgumentException("Inventory should be a menu!");
    }

    public static void initialize(Plugin plugin) {
        if (initialized) {
            throw new IllegalStateException("menus are already initialized");
        }

        Bukkit.getPluginManager().registerEvents(new Menus(), plugin);
        new MenuService(plugin, Bukkit.getPluginManager());
        initialized = true;
    }

    @EventHandler(ignoreCancelled = true)
    private void handleClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && Menus.isMenu(inventory)) {
            MenuSession session = Menus.getSession(inventory);
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

    @SuppressWarnings({"unchecked", "rawtypes"})
    @EventHandler(ignoreCancelled = true)
    private void handlePlayerInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        Player player = (Player) event.getWhoClicked();
        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (Menus.isMenu(topInventory) && inventory instanceof PlayerInventory) {
            MenuSession session = Menus.getSession(topInventory);
            Menu menu = session.getMenu();
            event.setCancelled(menu.isLockUserInventory());
            menu.processPlayerInventoryClick(session, event.getClick(), event.getSlot());
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @EventHandler
    private void handleOpen(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && Menus.isMenu(inventory)) {
            MenuSession session = Menus.getSession(inventory);
            Menu menu = session.getMenu();
            menu.processOpen(session);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @EventHandler
    private void handleClose(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory != null && Menus.isMenu(inventory)) {
            MenuSession session = Menus.getSession(inventory);
            Menu menu = session.getMenu();
            menu.processClose(session);
        }
    }

}
