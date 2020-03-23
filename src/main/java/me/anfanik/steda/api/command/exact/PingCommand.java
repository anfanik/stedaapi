package me.anfanik.steda.api.command.exact;

import me.anfanik.steda.api.command.Command;
import me.anfanik.steda.api.command.CommandHandler;
import me.anfanik.steda.api.command.SubcommandHandler;
import me.anfanik.steda.api.command.executor.Executor;
import me.anfanik.steda.api.command.executor.PlayerExecutor;

/**
 * @author Anfanik
 * Date: 05/01/2020
 */

public class PingCommand extends Command {

    public PingCommand() {
        super("ping");
    }

    @CommandHandler
    public void main(Executor<?> executor) {
        executor.sendCommandMessage("Pong!");
    }

    @SubcommandHandler({"pong"})
    public void pong(PlayerExecutor executor) {
        executor.sendCommandMessage("Ого какой, сейчас такое скажу, офигеешь.");
        executor.sendCommandMessage("Ping!");
    }

}