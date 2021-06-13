package me.anfanik.steda.api.menu;

import lombok.experimental.UtilityClass;
import lombok.val;
import me.anfanik.steda.api.menu.button.ClickCallback;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.click.MenuClick;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.menu.content.ContentProvider;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import me.anfanik.steda.api.menu.item.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@UtilityClass
@Deprecated
public class LegacyMenusHelper {

    public <S extends MenuSession> MenuClickHandler<S> toMenuClickHandler(ClickCallback<S> clickCallback) {
        return (session, click) -> clickCallback.process(session.getPlayer(), click.getType(), click.getSlot());
    }

    public <S extends MenuSession> ClickCallback toClickCallback(MenuClickHandler clickHandler) {
        return (player, click, slot) -> clickHandler.handle(new MenuSession(player), new MenuClick(click, slot));
    }

    public MenuItem toMenuItem(MenuButton menuButton) {
        return MenuItem.createDynamic(session -> menuButton.getItemStack(), menuButton.getCallbacks().stream()
                .map((Function<ClickCallback, MenuClickHandler>) LegacyMenusHelper::toMenuClickHandler)
                .toArray(MenuClickHandler[]::new));
    }

    public MenuButton toMenuButton(MenuSession session, MenuItem menuItem) {
        val icon = menuItem.getIcon(session);
        List<ClickCallback> clickCallbacks = new ArrayList<>();
        menuItem.getClickHandlers().forEach(clickHandler -> clickCallbacks.add(toClickCallback(clickHandler)));
        return new MenuButton(icon, clickCallbacks);
    }

    public <S extends MenuSession> ContentProvider<S> toContentProvider(FillingStrategy<S> fillingStrategy) {
        return (session, context) -> {
            val result = fillingStrategy.generate(session);
            result.forEach((slot, button) -> {
                context.addItem(slot, toMenuItem(button));
            });
        };
    }

}
