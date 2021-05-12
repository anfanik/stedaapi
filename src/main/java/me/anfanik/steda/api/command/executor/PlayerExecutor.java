package me.anfanik.steda.api.command.executor;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.access.AccessCheck;
import me.anfanik.steda.api.utility.ChatUtility;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PlayerExecutor implements Executor<Player> {

    public static final AccessCheck<Executor<?>> EXECUTOR_ACCESS_CHECK = AccessCheck.simple(Executor::isPlayer, "Эта команда доступна только игрокам!");

    public static PlayerExecutor get(Player player) {
        return new PlayerExecutor(player);
    }

    private final Player player;

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String message, Object... arguments) {
        ChatUtility.colored().send(player, message, arguments);
    }

    @Override
    public void sendCommandMessage(String message, Object... arguments) {
        ChatUtility.colored().send(player, COMMAND_MESSAGE_PREFIX + message, arguments);
    }

    public void sendCommandMessage(BaseComponent... components) {
        if (components.length == 0) {
            return;
        }
        Player player = handle();
        BaseComponent component = new TextComponent(Executor.COMMAND_MESSAGE_PREFIX);
        component.addExtra(components[0]);
        components[0] = component;
        player.spigot().sendMessage(components);
    }

    @Override
    public boolean isPlayer() {
        return true;
    }

    @Override
    public boolean isConsole() {
        return false;
    }

    public Player handle() {
        return player;
    }

}
