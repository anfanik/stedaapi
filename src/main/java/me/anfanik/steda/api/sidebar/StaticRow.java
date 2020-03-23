package me.anfanik.steda.api.sidebar;

import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class StaticRow implements Row {

    @Getter
    private final String id;

    private String text;

    private List<BiConsumer<Player, String>> updateCallbacks = new ArrayList<>();

    public void addUpdateCallback(BiConsumer<Player, String> callback) {
        updateCallbacks.add(callback);
    }

    public StaticRow(String message) {
        this(UUID.randomUUID().toString().substring(32), message);
    }

    public StaticRow(String id, String text){
        this.id = id;
        this.text = TextUtility.colorize(text);
    }

    @Override
    public void update(Player player) {
        updateCallbacks.forEach(callback -> callback.accept(player, text));
    }

}
