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
    private final Map<Character, PatternContent<S>> content;

    @Override
    public void provide(S session, ContentPreparingContext context) {
        content.values().forEach(content -> content.resetState(session));

        int slot = 0;
        for (String line : matrix) {
            for (char symbol : line.toCharArray()) {
                if (content.containsKey(symbol)) {
                    PatternContent<S> symbolContent = content.get(symbol);
                    context.addItem(slot, symbolContent.get(session));
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
        private final Map<Character, PatternContent<S>> content = new HashMap<>();

        public Builder<S> matrix(String... matrix) {
            this.matrix = matrix;
            return this;
        }

        public Builder<S> item(char symbol, MenuItem item) {
            content.put(symbol, new StaticPatternContent<>(item));
            return this;
        }

        public Builder<S> item(char symbol, Supplier<MenuItem> supplier) {
            content.put(symbol, new SupplierPatternContent<>(supplier));
            return this;
        }

        public Builder<S> item(char symbol, Function<S, MenuItem> function) {
            content.put(symbol, new FunctionPatternContent<>(function));
            return this;
        }

        public Builder<S> items(char symbol, Iterable<MenuItem> items, MenuItem defaultItem) {
            content.put(symbol, new IteratorPatternContent<>(ignored -> items.iterator(), defaultItem));
            return this;
        }

        public Builder<S> items(char symbol, Iterable<MenuItem> items) {
            return items(symbol, items, null);
        }

        public Builder<S> items(char symbol, Function<S, Iterator<MenuItem>> items, MenuItem defaultItem) {
            content.put(symbol, new IteratorPatternContent<>(items, defaultItem));
            return this;
        }

        public Builder<S> items(char symbol, Function<S, Iterator<MenuItem>> items) {
            return items(symbol, items, null);
        }

        public Builder<S> paginatedItems(char symbol, Iterable<MenuItem> items, int perPage, MenuItem defaultItem) {
            content.put(symbol, new PaginatedIteratorPatternContent<>(ignored -> items.iterator(), perPage, defaultItem));
            return this;
        }

        public Builder<S> paginatedItems(char symbol, Iterable<MenuItem> items, int perPage) {
            return paginatedItems(symbol, items, perPage, null);
        }

        public Builder<S> paginatedItems(char symbol, Function<S, Iterator<MenuItem>> items, int perPage, MenuItem defaultItem) {
            content.put(symbol, new PaginatedIteratorPatternContent<>(items, perPage, defaultItem));
            return this;
        }

        public Builder<S> paginatedItems(char symbol, Function<S, Iterator<MenuItem>> items, int perPage) {
            return paginatedItems(symbol, items, perPage, null);
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

    private interface PatternContent<S extends MenuSession> {

        MenuItem get(S session);

        void resetState(S session);

    }

    @RequiredArgsConstructor
    private static class StaticPatternContent<S extends MenuSession> implements PatternContent<S> {

        private final MenuItem item;

        @Override
        public MenuItem get(S session) {
            return item;
        }

        @Override
        public void resetState(S session) {
        }

    }

    private static class IteratorPatternContent<S extends MenuSession> implements PatternContent<S> {

        private final Function<S, Iterator<MenuItem>> iteratorProvider;
        private final MenuItem defaultItem;

        private Iterator<MenuItem> iterator;

        private IteratorPatternContent(Function<S, Iterator<MenuItem>> iteratorProvider, MenuItem defaultItem) {
            this.iteratorProvider = iteratorProvider;
            this.defaultItem = defaultItem;
        }


        @Override
        public MenuItem get(S session) {
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return defaultItem;
            }
        }

        @Override
        public void resetState(S session) {
            iterator = iteratorProvider.apply(session);
        }

    }

    private static class PaginatedIteratorPatternContent<S extends MenuSession> implements PatternContent<S> {

        private final Function<S, Iterator<MenuItem>> iteratorProvider;
        private final int perPage;
        private final MenuItem defaultItem;

        private Iterator<MenuItem> iterator;

        private PaginatedIteratorPatternContent(Function<S, Iterator<MenuItem>> iteratorProvider, int perPage, MenuItem defaultItem) {
            this.iteratorProvider = iteratorProvider;
            this.perPage = perPage;
            this.defaultItem = defaultItem;
        }


        @Override
        public MenuItem get(S session) {
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return defaultItem;
            }
        }

        @Override
        public void resetState(S session) {
            iterator = iteratorProvider.apply(session);
            for (int i = 0; i < perPage * session.getPage(); i++) {
                if (iterator.hasNext()) {
                    iterator.next();
                } else {
                    break;
                }
            }
        }

    }

    private static class FunctionPatternContent<S extends MenuSession> implements PatternContent<S> {

        private final Function<S, MenuItem> function;

        private FunctionPatternContent(Function<S, MenuItem> function) {
            this.function = function;
        }


        @Override
        public MenuItem get(S session) {
            return function.apply(session);
        }

        @Override
        public void resetState(S session) {
        }

    }

    private static class SupplierPatternContent<S extends MenuSession> implements PatternContent<S> {

        private final Supplier<MenuItem> supplier;

        private SupplierPatternContent(Supplier<MenuItem> supplier) {
            this.supplier = supplier;
        }


        @Override
        public MenuItem get(S session) {
            return supplier.get();
        }

        @Override
        public void resetState(S session) {
        }

    }

}
