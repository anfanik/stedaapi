package me.anfanik.steda.api.sidebar;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public interface Sidebar {

    Row[] getRows();

    default Row getRow(String id) {
        for (Row row : getRows()) {
            if (row.getId().equalsIgnoreCase(id)) {
                return row;
            }
        }
        return null;
    }

    default void update(Player player) {
        for (Row row : getRows()) {
            row.update(player);
        }
    }

    void show(Player player);

    void hide(Player player);

    static Builder builder() {
        return new Builder();
    }

    class Builder {

        private String name;
        private Function<Player, String> titleGenerator;
        private List<Row> rows = new ArrayList<>();

        public Builder() {
            UUID uuid = UUID.randomUUID();
            name = uuid.toString().substring(32);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(Function<Player, String> titleGenerator) {
            this.titleGenerator = titleGenerator;
            return this;
        }

        public Builder row(Row row) {
            rows.add(row);
            return this;
        }

        public Sidebar build() {
            SidebarImpl sidebar = new SidebarImpl(name, rows.toArray(new Row[0]));
            if (titleGenerator != null) {
                sidebar.setTitle(titleGenerator);
            }
            return sidebar;
        }

    }

}
