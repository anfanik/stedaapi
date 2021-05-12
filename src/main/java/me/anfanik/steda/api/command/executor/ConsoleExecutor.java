package me.anfanik.steda.api.command.executor;

import com.google.common.base.Preconditions;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.anfanik.steda.api.access.AccessCheck;
import me.anfanik.steda.api.utility.ChatUtility;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ConsoleExecutor implements Executor<CommandSender> {

    public static final AccessCheck<Executor<?>> EXECUTOR_ACCESS_CHECK = AccessCheck.simple(Executor::isConsole, "Эта команда доступна только серверу!");

    public static ConsoleExecutor get(CommandSender commandSender) {
        Preconditions.checkArgument(commandSender instanceof ConsoleCommandSender
                || commandSender instanceof RemoteConsoleCommandSender,
                "Command sender must be extends a ConsoleCommandSender");
        return new ConsoleExecutor(commandSender);
    }

    private final CommandSender consoleSender;

    @Override
    public String getName() {
        return consoleSender.getName();
    }

    @Override
    public void sendMessage(String message, Object... arguments) {
        ChatUtility.colored().send(consoleSender, message, arguments);
    }

    @Override
    public void sendCommandMessage(String message, Object... arguments) {
        ChatUtility.colored().send(consoleSender, COMMAND_MESSAGE_PREFIX + message, arguments);
    }

    @Override
    public boolean isPlayer() {
        return false;
    }

    @Override
    public boolean isConsole() {
        return true;
    }

    @Override
    public CommandSender handle() {
        return consoleSender;
    }

}
