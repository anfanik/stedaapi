package me.anfanik.steda.api.menu.button;

import me.anfanik.steda.api.menu.MenuSession;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

@FunctionalInterface
public interface ClickCallback<S extends MenuSession> {

    void process(Player player, ClickType clickType, int slot);

}
