package me.anfanik.steda.api.command;

import lombok.Getter;
import lombok.val;
import me.anfanik.steda.api.command.executor.ConsoleExecutor;
import me.anfanik.steda.api.command.executor.Executor;
import me.anfanik.steda.api.command.executor.PlayerExecutor;
import me.anfanik.steda.api.utility.ArrayUtility;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public abstract class Command extends CommandBase {

    private static final CommandMap commandMap;
    private static final Map<String, org.bukkit.command.Command> commandMapHandle;
    private static final Map<org.bukkit.command.Command, Command> stedaCommandMap =  new HashMap<>();

    static {
        CommandMap temporaryCommandMap = null;
        Map<String, org.bukkit.command.Command> temporaryCommandMapHandle = null;
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            temporaryCommandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            Field commandMapHandleField = SimpleCommandMap.class.getDeclaredField("knownCommands");
            commandMapHandleField.setAccessible(true);
            //noinspection unchecked
            temporaryCommandMapHandle = (Map<String, org.bukkit.command.Command>) commandMapHandleField.get(temporaryCommandMap);
        } catch (IllegalAccessException | NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        commandMap = temporaryCommandMap;
        commandMapHandle = temporaryCommandMapHandle;
    }

    public static Optional<Command> get(String alias) {
        val handle = commandMap.getCommand(alias);
        if (handle != null) {
            return Optional.ofNullable(stedaCommandMap.get(handle));
        }
        return Optional.empty();
    }

    public static boolean unregister(Command command) {
        stedaCommandMap.remove(command.getHandle());
        for (String alias : command.getAliases()) {
            commandMapHandle.remove(alias);
        }
        return false;
    }

    @Getter
    private final org.bukkit.command.Command handle;

    @Deprecated
    public Command(String[] aliases) {
        this(aliases[0], ArrayUtility.skipFirst(aliases, new String[aliases.length - 1], 1));
    }

    public Command(String label, String... aliases) {
        super(label, aliases);
        handle = new org.bukkit.command.Command(label, label + " command", "/" + label + " <arguments>", Arrays.asList(getAliases())) {
            @Override
            public boolean execute(CommandSender sender, String label, String[] arguments) {
                Executor<?> executor = null;
                if (sender instanceof ConsoleCommandSender) {
                    executor = ConsoleExecutor.get(sender);
                } else if (sender instanceof Player) {
                    executor = PlayerExecutor.get((Player) sender);
                } else {
                    try {
                        Class.forName("org.bukkit.command.RemoteConsoleCommandSender");
                        if (sender instanceof RemoteConsoleCommandSender) {
                            executor = ConsoleExecutor.get(sender);
                        }
                    } catch (ClassNotFoundException ignored) {
                    }
                }

                if (executor == null) {
                    sender.sendMessage("Не удалось выполнить команду: unknown executor type.");
                    return false;
                }

                try {
                    execute0(executor, arguments);
                } catch (Throwable throwable) {
                    UUID uuid = UUID.randomUUID();
                    System.out.println("Unhandled exception while processing /" + getName() + " command!");
                    System.out.println("Exception UUID: " + uuid);
                    throwable.printStackTrace();
                    if (executor instanceof PlayerExecutor) {
                        PlayerExecutor playerExecutor = (PlayerExecutor) executor;
                        playerExecutor.sendCommandMessage(new ComponentBuilder("§7Во время команды было сгенерированно неожиданное исключение!")
                                .append("\n")
                                .append("§7UUID ошибки: ")
                                .append("§e" + uuid.toString())
                                .event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, uuid.toString()))
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {new TextComponent("§7Нажмите, что бы скопировать")}))
                                .append("\n")
                                .append("§c" + throwable.getMessage())
                                .create());
                    } else {
                        executor.sendCommandMessage("&cВо время команды было сгенерированно неожидаемое исключение!");
                        executor.sendCommandMessage("&cID ошибки: " + uuid);
                        executor.sendMessage("&c" + throwable.getMessage());
                    }
                }
                return true;
            }
        };
        commandMap.register("steda", handle);
        loadSubcommands();
    }

}
