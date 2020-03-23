package me.anfanik.steda.api.sidebar;

import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public interface Row {

    String getId();

    void update(Player player);

    void addUpdateCallback(BiConsumer<Player, String> callback);

}
