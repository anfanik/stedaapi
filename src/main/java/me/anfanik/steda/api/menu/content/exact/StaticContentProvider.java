package me.anfanik.steda.api.menu.content.exact;

import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.content.ContentPreparingContext;
import me.anfanik.steda.api.menu.content.ContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;

import java.util.HashMap;
import java.util.Map;

public class StaticContentProvider implements ContentProvider<MenuSession> {

    private final Map<Integer, MenuItem> items = new HashMap<>();

    @Override
    public void provide(MenuSession session, ContentPreparingContext context) {
        items.forEach(context::addItem);
    }

    public void setItem(int slot, MenuItem item) {
        items.put(slot, item);
    }

}
