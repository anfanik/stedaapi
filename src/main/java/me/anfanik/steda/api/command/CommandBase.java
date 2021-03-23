package me.anfanik.steda.api.command;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.val;
import me.anfanik.steda.api.access.AccessCheck;
import me.anfanik.steda.api.command.executor.ConsoleExecutor;
import me.anfanik.steda.api.command.executor.Executor;
import me.anfanik.steda.api.command.executor.PlayerExecutor;
import me.anfanik.steda.api.utility.TextUtility;

import java.lang.reflect.Method;
import java.util.*;

public abstract class CommandBase {

    @Getter
    @Setter
    private boolean ignoreParentsChecks = false;

    @Getter
    private final String[] aliases;
    @Getter
    @Setter
    private CommandHandle handler;

    public CommandBase(String[] aliases) {
        this.aliases = aliases;
        loadHandle();
    }

    @Deprecated
    protected void execute(Executor<?> executor, String[] arguments) {
        executor.sendCommandMessage("Обработчик команды не найден!");
        throw new IllegalStateException("command handler is not set");
    }

    protected final void execute0(Executor<?> executor, String[] arguments) {
        CommandBase handle = this;
        if (arguments.length > 0) {
            val subcommand = subcommands.get(arguments[0].toLowerCase());
            if (subcommand != null) {
                handle = subcommand;
            }
        }

        if (handle == this || !handle.isIgnoreParentsChecks()) {
            Optional<AccessCheck<Executor<?>>> optional = getAccessChecks().stream()
                    .filter(check -> !check.checkAccess(executor))
                    .findAny();
            if (optional.isPresent()) {
                AccessCheck<Executor<?>> check = optional.get();
                executor.sendCommandMessage(check.getErrorMessage(executor));
                return;
            }
        }

        if (handle == this) {
            getHandler().execute(executor, arguments);
        } else {
            String[] subcommandArguments = new String[arguments.length - 1];
            System.arraycopy(arguments, 1, subcommandArguments, 0, arguments.length - 1);
            handle.execute0(executor, subcommandArguments);
        }
    }

    protected final void execute1(Executor<?> executor, String[] arguments) {
        int argument = 0;
        List<CommandBase> parents = new LinkedList<>();
        CommandBase handle = this;
        while (arguments.length > argument && handle.getSubcommands().containsKey(arguments[argument].toLowerCase())) {
            parents.add(handle);
            handle = handle.getSubcommands().get(arguments[argument]);
            argument++;
        }

        val checks = new ArrayList<>(handle.getAccessChecks());
        if (!isIgnoreParentsChecks() && !parents.isEmpty()) {
            val parentsReversed = new ArrayList<>(parents);
            Collections.reverse(parentsReversed);
            val iterator = parentsReversed.iterator();
            CommandBase parent;
            do {
                parent = iterator.next();
                checks.addAll(accessChecks);
            } while (!parent.isIgnoreParentsChecks() && iterator.hasNext());
        }

        Optional<AccessCheck<Executor<?>>> optional = checks.stream()
                .filter(check -> !check.checkAccess(executor))
                .findAny();
        if (optional.isPresent()) {
            AccessCheck<Executor<?>> check = optional.get();
            executor.sendCommandMessage(check.getErrorMessage(executor));
            return;
        }

        String[] handleArguments = new String[arguments.length - argument];
        System.arraycopy(arguments, argument, handleArguments, 0, arguments.length - argument);

        val handler = handle.getHandler();
        if (handler == null) {
            executor.sendCommandMessage("Обработчик команды не установлен!");
            throw new IllegalStateException("command handler is null");
        }
        handler.execute(executor, handleArguments);
    }

    private final Set<AccessCheck<Executor<?>>> accessChecks = new HashSet<>();

    public void addAccessCheck(AccessCheck<Executor<?>> check) {
        accessChecks.add(check);
    }

    public Set<AccessCheck<Executor<?>>> getAccessChecks() {
        return Collections.unmodifiableSet(accessChecks);
    }

    public CommandBase(String label, String... aliases) {
        this(toArray(label, aliases));
    }

    private static String[] toArray(String label, String[] aliases) {
        String[] result = new String[aliases.length + 1];
        result[0] = label;
        int index = 1;
        for (val alias : aliases) {
            result[index] = alias;
            index++;
        }
        return result;
    }

    @Getter
    private final Map<String, CommandBase> subcommands = new HashMap<>();

    public void addSubcommand(CommandBase subcommand) {
        if (subcommands.containsValue(subcommand)) {
            throw new IllegalStateException("subcommand is already registered");
        }
        for (val alias : subcommand.getAliases()) {
            subcommands.put(alias.toLowerCase(), subcommand);
        }
    }

    public String getLabel() {
        return aliases[0];
    }

    public void loadHandle() {
        for (Method method : getClass().getMethods()) {
            CommandHandler handler = method.getAnnotation(CommandHandler.class);
            if (handler == null) {
                continue;
            }
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1 && parameters.length != 2) {
                continue;
            }

            Class<?> first = parameters[0];
            AccessCheck<Executor<?>> accessCheck;
            if (first == Executor.class) {
                accessCheck = executor -> true;
            } else if (first == PlayerExecutor.class) {
                accessCheck = PlayerExecutor.EXECUTOR_ACCESS_CHECK;
            } else if (first == ConsoleExecutor.class) {
                accessCheck = ConsoleExecutor.EXECUTOR_ACCESS_CHECK;
            } else {
                continue;
            }

            boolean useArguments;
            if (parameters.length == 2) {
                Class<?> second = parameters[1];
                if (second != String[].class) {
                    continue;
                }
                useArguments = true;
            } else {
                useArguments = false;
            }

            this.handler = new CommandHandle() {
                @SneakyThrows
                public void execute(Executor<?> executor, String[] arguments) {
                    if (accessCheck.checkAccess(executor)) {
                        if (useArguments) {
                            method.invoke(CommandBase.this, parameters[0].cast(executor), arguments);
                        } else {
                            method.invoke(CommandBase.this, parameters[0].cast(executor));
                        }
                    } else {
                        executor.sendCommandMessage(accessCheck.getErrorMessage(executor));
                    }
                }
            };
        }
        //Legacy support
        if (handler == null) {
            handler = new CommandHandle() {
                @Override
                public void execute(Executor<?> executor, String[] arguments) {
                    CommandBase.this.execute(executor, arguments);
                }
            };
        }
    }

    public void loadSubcommands() {
        addSubcommand(new CommandBase("help") {
            @Override
            protected void execute(Executor<?> executor, String[] arguments) {
                val subcommands = CommandBase.this.getSubcommands();
                if (subcommands.size() > 0) {
                    List<String> lines = new ArrayList<>();
                    lines.add("Подкоманды:");
                    subcommands.values().stream()
                            .distinct()
                            .filter(subcommand -> subcommand != this)
                            .forEach(subcommand -> lines.add("&e  " + subcommand.getLabel()));
                    executor.sendCommandMessage(TextUtility.mergeLines(lines));
                } else {
                    executor.sendCommandMessage("Подкоманды не найдены.");
                }
            }
        });

        for (Method method : getClass().getMethods()) {
            SubcommandHandler handler = method.getAnnotation(SubcommandHandler.class);
            if (handler == null) {
                continue;
            }
            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1 && parameters.length != 2) {
                continue;
            }

            Class<?> first = parameters[0];
            AccessCheck<Executor<?>> accessCheck;
            if (first == Executor.class) {
                accessCheck = executor -> true;
            } else if (first == PlayerExecutor.class) {
                accessCheck = PlayerExecutor.EXECUTOR_ACCESS_CHECK;
            } else if (first == ConsoleExecutor.class) {
                accessCheck = ConsoleExecutor.EXECUTOR_ACCESS_CHECK;
            } else {
                continue;
            }

            boolean useArguments;
            if (parameters.length == 2) {
                Class<?> second = parameters[1];
                if (second != String[].class) {
                    continue;
                }
                useArguments = true;
            } else {
                useArguments = false;
            }

            CommandBase handle = new CommandBase(handler.value()) {
                @CommandHandler
                @SneakyThrows
                protected void execute(Executor<?> executor, String[] arguments) {
                    if (accessCheck.checkAccess(executor)) {
                        if (useArguments) {
                            method.invoke(CommandBase.this, parameters[0].cast(executor), arguments);
                        } else {
                            method.invoke(CommandBase.this, parameters[0].cast(executor));
                        }
                    } else {
                        executor.sendCommandMessage(accessCheck.getErrorMessage(executor));
                    }
                }
            };
            handle.setIgnoreParentsChecks(handle.isIgnoreParentsChecks());
            handle.loadSubcommands();
            addSubcommand(handle);
        }
    }

}
