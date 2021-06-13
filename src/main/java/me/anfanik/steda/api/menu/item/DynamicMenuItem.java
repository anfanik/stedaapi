package me.anfanik.steda.api.menu.item;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DynamicMenuItem implements MenuItem {

    private final Function<MenuSession, ItemStack> iconGenerator;
    private final List<MenuClickHandler<?>> clickHandlers;

    @Override
    public ItemStack getIcon(MenuSession session) {
        return iconGenerator.apply(session);
    }

    @Override
    public Collection<MenuClickHandler<?>> getClickHandlers() {
        return Collections.unmodifiableList(clickHandlers);
    }

}
