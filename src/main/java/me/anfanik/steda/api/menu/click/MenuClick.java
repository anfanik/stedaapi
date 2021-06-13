package me.anfanik.steda.api.menu.click;

import lombok.Value;
import org.bukkit.event.inventory.ClickType;

@Value
public class MenuClick {

    ClickType type;
    int slot;

}
