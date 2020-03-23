package me.anfanik.steda.api.sidebar;

import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class DynamicRow implements Row {

    @Getter
    private final String id;

    private final Function<Player, String> textGenerator;

    private List<BiConsumer<Player, String>> updateCallbacks = new ArrayList<>();

    public void addUpdateCallback(BiConsumer<Player, String> callback) {
        updateCallbacks.add(callback);
    }

    public DynamicRow(Function<Player, String> textGenerator) {
        this(UUID.randomUUID().toString().substring(32), textGenerator);
    }

    public DynamicRow(String id, Function<Player, String> textGenerator){
        this.id = id;
        this.textGenerator = textGenerator;
    }

    @Override
    public void update(Player player) {
        updateCallbacks.forEach(callback -> callback.accept(player, TextUtility.colorize(textGenerator.apply(player))));
    }

}

