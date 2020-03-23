package me.anfanik.steda.api.menu.button;

import me.anfanik.steda.api.menu.MenuSession;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Anfanik
 * Date: 20/09/2019
 */

@FunctionalInterface
public interface ClickCallback<S extends MenuSession> {

    void process(Player player, ClickType clickType, int slot);

}
