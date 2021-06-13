package me.anfanik.steda.api.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import me.anfanik.steda.api.menu.button.ClickCallback;
import me.anfanik.steda.api.menu.click.MenuClick;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.menu.content.ContentProvider;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class Menu<S extends MenuSession> {

    private static final Function<Player, MenuSession> DEFAULT_SESSION_GENERATOR = MenuSession::new;

    private final InventoryType inventoryType;
    private final int slots;

    private final Function<Player, S> sessionGenerator;
    @Setter
    @Getter
    private Function<S, String> titleGenerator;
    private final List<ContentProvider<S>> contentProviders = new ArrayList<>();

    @Getter
    @Setter
    private boolean lockUserInventory = true;

    private final List<Consumer<S>> openCallbacks = new ArrayList<>();
    private final List<Consumer<S>> closeCallbacks = new ArrayList<>();
    private final List<MenuClickHandler<S>> playerInventoryClickCallbacks = new ArrayList<>();

    public Menu(InventoryType inventoryType, int slots, Function<Player, S> sessionGenerator) {
        this.inventoryType = inventoryType;
        this.slots = slots;
        this.sessionGenerator = sessionGenerator;
        titleGenerator = ignored -> inventoryType.getDefaultTitle();
    }

    @Deprecated
    public Menu(InventoryType inventoryType, int slots) {
        this(inventoryType, slots, (Function<Player, S>) DEFAULT_SESSION_GENERATOR);
    }

    @Deprecated
    public Menu(InventoryType inventoryType) {
        this(inventoryType, inventoryType.getDefaultSize());
    }

    @Deprecated
    public Menu(int slots) {
        this(InventoryType.CHEST, slots);
    }

    public MenuSession open(Player player) {
        S session = sessionGenerator.apply(player);
        open(session);
        return session;
    }

    public void open(S session) {
        String title = TextUtility.colorize(titleGenerator.apply(session));
        Inventory inventory = inventoryType == InventoryType.CHEST
                ? Bukkit.createInventory(session, slots, title)
                : Bukkit.createInventory(session, inventoryType, title);

        session.initialize(this, inventory, contentProviders);

        session.update();
        session.getPlayer().openInventory(inventory);
    }

    boolean processClick(MenuSession session, ClickType clickType, int slot) {
        val item = session.getContentContext().getLatestContent().get(slot);
        if (item != null) {
            val click = new MenuClick(clickType, slot);
            //noinspection unchecked,rawtypes
            item.getClickHandlers().forEach(handler -> ((MenuClickHandler) handler).handle(session, click));
        }
        return true;
    }

    void processPlayerInventoryClick(S session, ClickType clickType, int slot) {
        val click = new MenuClick(clickType, slot);
        playerInventoryClickCallbacks.forEach(handler -> handler.handle(session, click));
    }

    void processOpen(S session) {
        openCallbacks.forEach(callback -> callback.accept(session));
    }

    void processClose(S session) {
        closeCallbacks.forEach(callback -> callback.accept(session));
    }

    public void addContentProvider(ContentProvider<S> contentProvider) {
        contentProviders.add(contentProvider);
    }

    public void onOpen(Consumer<S> callback) {
        openCallbacks.add(callback);
    }

    public void onClose(Consumer<S> callback) {
        closeCallbacks.add(callback);
    }

    public void onPlayerInventoryClick(MenuClickHandler<S> handler) {
        playerInventoryClickCallbacks.add(handler);
    }

    @Deprecated
    public void setFillingStrategy(FillingStrategy<S> strategy) {
        addContentProvider(LegacyMenusHelper.toContentProvider(strategy));
    }

    @Deprecated
    public void addOpenCallback(Consumer<S> callback) {
        onOpen(callback);
    }

    @Deprecated
    public void addCloseCallback(Consumer<S> callback) {
        onClose(callback);
    }

    public List<ContentProvider<S>> getContentProviders() {
        return Collections.unmodifiableList(contentProviders);
    }

    @Deprecated
    public void addPlayerInventoryCallback(ClickCallback callback) {
        playerInventoryClickCallbacks.add(LegacyMenusHelper.toMenuClickHandler(callback));
    }

    public static <S extends MenuSession> Builder<S> builder(Class<? extends S> clazz) {
        return new Builder<>();
    }

    public static Builder<MenuSession> builder() {
        return builder(MenuSession.class);
    }

    public static class Builder<S extends MenuSession> {

        private InventoryType type = InventoryType.CHEST;
        private int size = -1;

        private Function<Player, S> sessionGenerator;
        private Function<S, String> titleGenerator = session -> "Меню";
        private final List<ContentProvider<S>> contentProviders = new ArrayList<>();

        private boolean lockUserInventory = true;

        private final List<Consumer<S>> openCallbacks = new ArrayList<>();
        private final List<Consumer<S>> closeCallbacks = new ArrayList<>();
        private final List<MenuClickHandler<S>> playerInventoryClickCallbacks = new ArrayList<>();

        public Builder<S> type(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder<S> size(int size) {
            this.size = size;
            return this;
        }

        public Builder<S> chestRows(int rows) {
            return size(rows * 9);
        }

        public Builder<S> title(Function<S, String> titleGenerator) {
            this.titleGenerator = titleGenerator;
            return this;
        }

        public Builder<S> title(String title) {
            this.titleGenerator = session -> title;
            return this;
        }

        public Builder<S> contentProvider(ContentProvider<S> contentProvider) {
            contentProviders.add(contentProvider);
            return this;
        }

        public Builder<S> lockUserInventory() {
            lockUserInventory = true;
            return this;
        }

        public Builder<S> onPlayerInventoryClick(MenuClickHandler<S> handler) {
            playerInventoryClickCallbacks.add(handler);
            return this;
        }

        public Builder<S> onOpen(Consumer<S> callback) {
            openCallbacks.add(callback);
            return this;
        }

        public Builder<S> onClose(Consumer<S> callback) {
            closeCallbacks.add(callback);
            return this;
        }

        public Builder<S> sessionGenerator(Function<Player, S> sessionGenerator) {
            this.sessionGenerator = sessionGenerator;
            return this;
        }

        @Deprecated
        public Builder<S> setType(InventoryType type) {
            return type(type);
        }

        @Deprecated
        public Builder<S> setSize(int size) {
            return size(size);
        }

        @Deprecated
        public Builder<S> setTitleGenerator(Function<S, String> titleGenerator) {
            return title(titleGenerator);
        }

        @Deprecated
        public Builder<S> setFillingStrategy(FillingStrategy<S> fillingStrategy) {
            return contentProvider(LegacyMenusHelper.toContentProvider(fillingStrategy));
        }

        @Deprecated
        public Builder<S> setLockUserInventory(boolean lockUserInventory) {
            this.lockUserInventory = lockUserInventory;
            return this;
        }

        @Deprecated
        public Builder<S> addOpenCallback(Consumer<S> callback) {
            return onOpen(callback);
        }

        @Deprecated
        public Builder<S> addCloseCallback(Consumer<S> callback) {
            return onClose(callback);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Deprecated
        public Builder<S> addPlayerInventoryCallback(ClickCallback callback) {
            return onPlayerInventoryClick(LegacyMenusHelper.toMenuClickHandler(callback));
        }

        public Menu<S> build() {
            if (size == -1) {
                size = type.getDefaultSize();
            }

            Menu<S> menu;
            if (sessionGenerator == null) {
                menu = new Menu<S>(type, size, (Function<Player, S>) DEFAULT_SESSION_GENERATOR);
            } else {
                menu = new Menu<>(type, size, sessionGenerator);
            }

            menu.setTitleGenerator(titleGenerator);
            contentProviders.forEach(menu::addContentProvider);
            menu.setLockUserInventory(lockUserInventory);

            openCallbacks.forEach(menu::onOpen);
            closeCallbacks.forEach(menu::onClose);
            playerInventoryClickCallbacks.forEach(menu::onPlayerInventoryClick);

            return menu;
        }

    }

}
