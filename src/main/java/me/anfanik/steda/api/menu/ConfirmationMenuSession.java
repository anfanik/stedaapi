package me.anfanik.steda.api.menu;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@Getter
public class ConfirmationMenuSession extends MenuSession {

    private final Consumer<Player> confirm;
    private final Consumer<Player> cancel;

    public ConfirmationMenuSession(Player player, Consumer<Player> confirm) {
        this(player, confirm, ignored -> {});
    }

    public ConfirmationMenuSession(
            Player player,
            Consumer<Player> confirm,
            Consumer<Player> cancel
    ) {
        super(player);
        this.confirm = confirm;
        this.cancel = cancel;
    }

}