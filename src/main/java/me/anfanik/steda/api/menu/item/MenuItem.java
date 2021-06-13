package me.anfanik.steda.api.menu.item;

import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

public interface MenuItem {

    static MenuItem createStatic(ItemStack icon, MenuClickHandler<?>... clickHandlers) {
        return new StaticMenuItem(icon, Arrays.asList(clickHandlers));
    }

    static MenuItem createStatic(ItemBuilder<?> iconBuilder, MenuClickHandler<?>... clickHandlers) {
        return createStatic(iconBuilder.build(), clickHandlers);
    }

    @SuppressWarnings("unchecked")
    static <S extends MenuSession> MenuItem createDynamic(Function<S, ItemStack> iconGenerator, MenuClickHandler<?>... clickHandlers) {
        return new DynamicMenuItem((Function<MenuSession, ItemStack>) iconGenerator, Arrays.asList(clickHandlers));
    }

    ItemStack getIcon(MenuSession session);

    Collection<MenuClickHandler<?>> getClickHandlers();

}
