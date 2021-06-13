package me.anfanik.steda.api.menu.content.exact;

import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.menu.MenuSession;
import me.anfanik.steda.api.menu.click.MenuClickHandler;
import me.anfanik.steda.api.menu.content.ContentPreparingContext;
import me.anfanik.steda.api.menu.content.ContentProvider;
import me.anfanik.steda.api.menu.item.MenuItem;
import me.anfanik.steda.api.utility.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PatternContentProvider<S extends MenuSession> implements ContentProvider<S> {

    private final String[] matrix;
    private final Map<Character, Function<S, MenuItem>> content;

    @Override
    public void provide(S session, ContentPreparingContext context) {
        int slot = 0;
        for (String line : matrix) {
            for (char symbol : line.toCharArray()) {
                if (content.containsKey(symbol)) {
                    Function<S, MenuItem> itemFunction = content.get(symbol);
                    context.addItem(slot, itemFunction.apply(session));
                }
                slot++;
            }
        }
    }

    public static <S extends MenuSession> Builder<S> builder() {
        return new Builder<>();
    }

    public static <S extends MenuSession> Builder<S> builder(Class<S> clazz) {
        return new Builder<>();
    }

    public static class Builder<S extends MenuSession> {

        private String[] matrix;
        private final Map<Character, Function<S, MenuItem>> content = new HashMap<>();

        public Builder<S> matrix(String... matrix) {
            this.matrix = matrix;
            return this;
        }

        public Builder<S> item(char symbol, MenuItem item) {
            content.put(symbol, session -> item);
            return this;
        }

        public Builder<S> item(char symbol, Supplier<MenuItem> itemSupplier) {
            content.put(symbol, session -> itemSupplier.get());
            return this;
        }

        public Builder<S> item(char symbol, Function<S, MenuItem> itemFunction) {
            content.put(symbol, itemFunction);
            return this;
        }

        public Builder<S> items(char symbol, Iterator<MenuItem> items, MenuItem defaultItem) {
            content.put(symbol, session -> {
                if (items.hasNext()) {
                    return items.next();
                } else {
                    return defaultItem;
                }
            });
            return this;
        }

        public Builder<S> items(char symbol, Iterator<MenuItem> items) {
            return items(symbol, items, null);
        }

        public Builder<S> items(char symbol, Iterable<MenuItem> items, MenuItem defaultItem) {
            return items(symbol, items.iterator(), defaultItem);
        }

        public Builder<S> items(char symbol, Iterable<MenuItem> items) {
            return items(symbol, items.iterator(), null);
        }

        public Builder<S> staticItem(char symbol, ItemStack icon, MenuClickHandler<?>... clickHandlers) {
            return item(symbol, MenuItem.createStatic(icon, clickHandlers));
        }

        public Builder<S> staticItem(char symbol, ItemBuilder<?> iconBuilder, MenuClickHandler<?>... clickHandlers) {
            return item(symbol, MenuItem.createStatic(iconBuilder, clickHandlers));
        }

        public PatternContentProvider<S> build() {
            Preconditions.checkNotNull(matrix, "matrix is not set");
            return new PatternContentProvider<>(matrix, content);
        }

    }

}
