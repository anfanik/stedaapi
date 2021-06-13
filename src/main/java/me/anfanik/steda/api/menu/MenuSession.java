package me.anfanik.steda.api.menu;

import lombok.Data;
import lombok.Getter;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.content.ContentPreparingContext;
import me.anfanik.steda.api.menu.content.ContentProvider;
import me.anfanik.steda.api.menu.content.exact.StaticContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.*;

@Data
public class MenuSession implements InventoryHolder {

    private final Player player;

    private Menu<?> menu;
    private Inventory inventory;
    private ContentPreparingContext contentContext;
    @Deprecated
    private StaticContentProvider legacySetItemContentProvider;

    public MenuSession(Player player) {
        this.player = player;
    }

    <S extends MenuSession> void initialize(Menu<S> menu, Inventory inventory, Collection<ContentProvider<S>> contentProviders) {
        this.menu = menu;
        this.inventory = inventory;
        contentContext = new ContentPreparingContext(new ArrayList<>(contentProviders));
    }

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
        /*CraftInventory craftInventory = (CraftInventory) inventory;
        IInventory nmsInventory = craftInventory.getInventory();
        CraftInventoryCustom.MinecraftInventory minecraftInventory = (InventorySubcontainer) nmsInventory;
        Function<MenuSession, String> titleGenerator = menu.getTitleGenerator();
        inventorySubcontainer.a(titleGenerator.apply(this));*/

        contentContext.update(this);
        contentContext.refill(this, inventory);

        updateInventory();
    }

    public void updateInventory() {
        Inventory openInventory = player.getOpenInventory().getTopInventory();
        if (openInventory != null && Menus.isMenu(openInventory)
                && Menus.getSession(openInventory) == this) {
            player.updateInventory();
        }
    }

    @Deprecated
    public void setItem(int slot, MenuItem item) {
        if (legacySetItemContentProvider == null)  {
            legacySetItemContentProvider = new StaticContentProvider();
        }

        legacySetItemContentProvider.setItem(slot, item);
        update();
    }

    @Deprecated
    public void setItem(int slot, MenuButton button) {
        setItem(slot, LegacyMenusHelper.toMenuItem(button));
    }

    @Deprecated
    public Map<Integer, MenuButton> getButtons() {
        Map<Integer, MenuButton> result = new HashMap<>();
        contentContext.getLatestContent().forEach((slot, item) -> {
            result.put(slot, LegacyMenusHelper.toMenuButton(this, item));
        });
        return result;
    }
}
