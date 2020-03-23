package me.anfanik.steda.api.command;

import me.anfanik.steda.api.command.executor.Executor;

public abstract class CommandHandle {

  public abstract void execute(Executor<?> executor, String[] arguments);

}
