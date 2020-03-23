package me.anfanik.steda.api.command.executor;

import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.command.CommandSender;

/**
 * @author Anfanik
 * Date: 18/09/2019
 */


public interface Executor<S extends CommandSender> {

    String COMMAND_MESSAGE_PREFIX = TextUtility.colorize("&8[&6Команды&8] &7");

    String getName();

    void sendMessage(String message, Object... arguments);

    void sendCommandMessage(String message, Object... arguments);

    boolean isPlayer();

    boolean isConsole();

    S handle();

}
