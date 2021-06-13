package me.anfanik.steda.api.menu;

import me.anfanik.steda.api.menu.button.ClickCallback;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

@Deprecated
public class MenuService {

    private static MenuService instance;

    public static MenuService get() {
        return instance;
    }

    public MenuService(Plugin plugin, PluginManager pluginManager) {
        instance = this;
    }

    @Deprecated
    public Menu<MenuSession> confirmationMenu(ClickCallback<?> confirmationCallback) {
        return confirmationMenu(confirmationCallback, (ignored1, ignored2, ignored3) -> {});
    }

    @Deprecated
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

}
