package me.anfanik.steda.api.menu;

import lombok.Getter;
import lombok.Setter;
import me.anfanik.steda.api.menu.button.MenuButton;
import me.anfanik.steda.api.menu.filling.FillingStrategy;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Anfanik
 * Date: 18/09/2019
 */

public class Menu<S extends MenuSession> {

    private final InventoryType inventoryType;
    private final int slots;

    @Getter
    @Setter
    private boolean lockUserInventory = true;

    @Setter
    @Getter
    private Function<S, String> titleGenerator;

    @Setter
    @Getter
    private FillingStrategy<S> fillingStrategy = ignored -> new HashMap<>();

    private static final Function<Player, MenuSession> DEFAULT_SESSION_GENERATOR = MenuSession::new;

    private final Function<Player, S> sessionGenerator;

    private final List<Consumer<S>> openCallbacks = new ArrayList<>();

    public void addOpenCallback(Consumer<S> callback) {
        openCallbacks.add(callback);
    }

    private final List<Consumer<S>> closeCallbacks = new ArrayList<>();

    public void addCloseCallback(Consumer<S> callback) {
        closeCallbacks.add(callback);
    }

    public Menu(InventoryType inventoryType, int slots, Function<Player, S> sessionGenerator) {
        this.inventoryType = inventoryType;
        this.slots = slots;
        this.sessionGenerator = sessionGenerator;
        titleGenerator = ignored -> inventoryType.getDefaultTitle();

    }

    public Menu(InventoryType inventoryType, int slots) {
        this(inventoryType, slots, (Function<Player, S>) DEFAULT_SESSION_GENERATOR);
    }

    public Menu(InventoryType inventoryType) {
        this(inventoryType, inventoryType.getDefaultSize());
    }

    public Menu(int slots) {
        this(InventoryType.CHEST, slots);
    }

    public MenuSession open(Player player) {
        S session = sessionGenerator.apply(player);
        open(session);
        return session;
    }

    public void open(S session) {
        Player player = session.getPlayer();
        session.setMenu(this);
        String title = TextUtility.colorize(titleGenerator.apply(session));
        Inventory inventory = inventoryType == InventoryType.CHEST
                ? Bukkit.createInventory(session, slots, title)
                : Bukkit.createInventory(session, inventoryType, title);
        session.setInventory(inventory);
        session.update();
        player.openInventory(inventory);
    }

    boolean processClick(MenuSession session, ClickType clickType, int slot) {
        MenuButton button = session.getButtons().get(slot);
        if (button != null) {
            Player player = session.getPlayer();
            button.processClick(player, clickType, slot);
        }
        return true;
    }

    void processOpen(S session) {
        openCallbacks.forEach(callback -> callback.accept(session));
    }

    void processClose(S session) {
        closeCallbacks.forEach(callback -> callback.accept(session));
    }

    public static boolean isMenu(Inventory inventory) {
        return inventory.getHolder() instanceof MenuSession;
    }

    public static MenuSession getSession(Inventory inventory) {
        if (isMenu(inventory)) {
            return (MenuSession) inventory.getHolder();
        }
        throw new IllegalArgumentException("Inventory should be a menu!");
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

        @SuppressWarnings("unchecked")
        private Function<Player, S> sessionGenerator = (Function<Player, S>) DEFAULT_SESSION_GENERATOR;

        private Function<S, String> titleGenerator = session -> "Инвентарь";

        private FillingStrategy<S> fillingStrategy = session -> new HashMap<>();

        private boolean lockUserInventory = false;

        private List<Consumer<S>> openCallbacks = new ArrayList<>();

        private List<Consumer<S>> closeCallbacks = new ArrayList<>();

        public Builder<S> setType(InventoryType type) {
            this.type = type;
            return this;
        }

        public Builder<S> setSize(int size) {
            this.size = size;
            return this;
        }

        public Builder<S> setTitleGenerator(Function<S, String> titleGenerator) {
            this.titleGenerator = titleGenerator;
            return this;
        }

        public Builder<S> setFillingStrategy(FillingStrategy<S> fillingStrategy) {
            this.fillingStrategy = fillingStrategy;
            return this;
        }

        public Builder<S> setLockUserInventory(boolean lockUserInventory) {
            this.lockUserInventory = lockUserInventory;
            return this;
        }

        public Builder<S> addOpenCallback(Consumer<S> callback) {
            openCallbacks.add(callback);
            return this;
        }

        public Builder<S> addCloseCallback(Consumer<S> callback) {
            closeCallbacks.add(callback);
            return this;
        }

        public Menu<S> build() {
            if (size == -1) {
                size = type.getDefaultSize();
            }

            Menu<S> menu = new Menu<>(type, size, sessionGenerator);
            menu.setTitleGenerator(titleGenerator);
            menu.setFillingStrategy(fillingStrategy);
            menu.setLockUserInventory(lockUserInventory);
            openCallbacks.forEach(menu::addOpenCallback);
            closeCallbacks.forEach(menu::addCloseCallback);

            return menu;
        }

    }

}
