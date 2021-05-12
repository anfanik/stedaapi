package me.anfanik.steda.api.menu.button;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class MenuButton {

    @Getter
    @Setter
    private ItemStack itemStack;
    private final List<ClickCallback> callbacks;

    public MenuButton(ItemStack itemStack) {
        this(itemStack, new ClickCallback[] {});
    }

    public MenuButton(ItemStack itemStack, ClickCallback... callbacks) {
        this(itemStack, new ArrayList<>(Arrays.asList(callbacks)));
    }

    public boolean processClick(Player player, ClickType clickType, int slot) {
        callbacks.forEach(callback -> callback.process(player, clickType, slot));
        return true;
    }

    public void addCallback(ClickCallback callback) {
        callbacks.add(callback);
    }

}