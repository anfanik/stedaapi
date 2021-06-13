package me.anfanik.steda.api.menu.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class StaticMenuItem implements MenuItem {

    private final ItemStack icon;
    private final List<MenuClickHandler<?>> clickHandlers;

    @Override
    public ItemStack getIcon(MenuSession session) {
        return icon;
    }

    @Override
    public Collection<MenuClickHandler<?>> getClickHandlers() {
        return Collections.unmodifiableList(clickHandlers);
    }

}
