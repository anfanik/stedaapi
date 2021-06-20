package me.anfanik.steda.api.menu.content;

import lombok.val;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.item.MenuItem;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class ContentPreparingContext {

    private final Collection<ContentProvider<?>> contentProviders;
    private final Map<Integer, List<MenuItem>> items = new HashMap<>();
    private Map<Integer, MenuItem> latestContent = new HashMap<>();

    public ContentPreparingContext(Collection<ContentProvider<?>> contentProviders) {
        this.contentProviders = contentProviders;
    }

    public ContentPreparingContext setItem(int slot, MenuItem item) {
        items.computeIfAbsent(slot, ArrayList::new).add(0, item);
        return this;
    }

    public ContentPreparingContext addItem(int slot, MenuItem item) {
        items.computeIfAbsent(slot, ArrayList::new).add(item);
        return this;
    }

    public void update(MenuSession session) {
        items.clear();
        contentProviders.forEach(provider -> {
            //noinspection unchecked,rawtypes
            ((ContentProvider) provider).provide(session, this);
        });
    }

    public void refill(MenuSession session, Inventory inventory) {
        inventory.clear();
        latestContent = new HashMap<>();
        items.forEach((slot, items) -> {
            for (MenuItem item : items) {
                if (item != null){
                    val icon = item.getIcon(session);
                    if (icon != null) {
                        inventory.setItem(slot, icon);
                        latestContent.put(slot, item);
                        break;
                    }
                }
            }
        });
    }

    public Map<Integer, List<MenuItem>> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public Map<Integer, MenuItem> getLatestContent() {
        return Collections.unmodifiableMap(latestContent);
    }

}
