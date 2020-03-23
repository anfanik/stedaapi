package me.anfanik.steda.api.menu;

import lombok.Data;
import lombok.Getter;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Anfanik
 * Date: 19/09/2019
 */

@Data
public class MenuSession implements InventoryHolder {

    private Menu<?> menu;

    private final Player player;

    private Inventory inventory;

    private Map<Integer, MenuButton> buttons = new HashMap<>();

    @Getter
    private int page = 0;

    public boolean nextPage() {
        page++;
        update();
        return true;
    }

    public boolean previousPage() {
        if (page == 0) {
            return false;
        }
        page--;
        update();
        return true;
    }

    public void update() {
        inventory.clear();

        /*CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory nmsInventory = craftInventory.getInventory();
        CraftInventoryCustom.MinecraftInventory minecraftInventory = (InventorySubcontainer) nmsInventory;
        Function<MenuSession, String> titleGenerator = menu.getTitleGenerator();
        inventorySubcontainer.a(titleGenerator.apply(this));*/

        FillingStrategy fillingStrategy = menu.getFillingStrategy();
        //noinspection unchecked
        buttons = fillingStrategy.generate(this);
        buttons.forEach((slot, button) -> inventory.setItem(slot, button.getItemStack()));
        updateInventory();
    }

    public void updateInventory() {
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        if (openInventory != null && Menu.isMenu(openInventory)
                && Menu.getSession(openInventory) == this) {
            player.updateInventory();
        }
    }

    public void setItem(int slot, MenuButton button) {
        buttons.put(slot, button);
        inventory.setItem(slot, button.getItemStack());
    }

}
