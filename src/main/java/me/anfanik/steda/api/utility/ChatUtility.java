package me.anfanik.steda.api.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.anfanik.steda.api.command.executor.Executor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatUtility {

    public static ChatUtility getPrefixed(String prefix) {
        return new ChatUtility(TextUtility.colorize(prefix));
    }

    @Getter
    private final String prefix;
    private final SendMessageStrategy prefixedStrategy;

    private ChatUtility(String prefix) {
        this.prefix = prefix;
        prefixedStrategy = (commandSender, message, arguments) ->
                commandSender.sendMessage(TextUtility.colorize(prefix + " &7" + message, arguments));
    }

    public SendMessageStrategy prefixed() {
        return prefixedStrategy;
    }

    private static SendMessageStrategy coloredStrategy = (commandSender, message, arguments) ->
            commandSender.sendMessage(TextUtility.colorize(message, arguments));

    public static SendMessageStrategy colored() {
        return coloredStrategy;
    }

    public interface SendMessageStrategy {

        void send(CommandSender commandSender, String message, Object... arguments);

        default void send(Executor executor, String message, Object... arguments) {
            executor.sendMessage(message, arguments);
        }

        default void sendToAll(String message, Object... arguments) {
            Bukkit.getOnlinePlayers().forEach(player -> send(player, message, arguments));
        }

    }

    public interface Message {

        ChatMessageType getType();

        void setType(ChatMessageType type);

        void send(CommandSender commandSender);

        void sendToAll();

        void sendToAllWithConsole();

        static Message fromBuilder(ComponentBuilder builder) {
            return new MessageImpl(builder.create());
        }

        static Message fromBuilderWithType(ComponentBuilder builder, ChatMessageType type) {
            Message message = new MessageImpl(builder.create());
            message.setType(type);
            return message;
        }
    }

    @RequiredArgsConstructor
    private static class MessageImpl implements Message {

        @Setter
        @Getter
        private ChatMessageType type; //TODO
        private final BaseComponent[] components;

        @Override
        public void send(CommandSender commandSender) {
            if (commandSender instanceof Player) {
                Player player = (Player) commandSender;
                player.spigot().sendMessage(components);
            } else {
                StringBuilder builder = new StringBuilder();
                for (BaseComponent component : components) {
                    builder.append(component.toLegacyText());
                }
                commandSender.sendMessage(builder.toString());
            }
        }

        @Override
        public void sendToAll() {
            Bukkit.getOnlinePlayers().forEach(this::send);
        }

        @Override
        public void sendToAllWithConsole() {
            sendToAll();
            send(Bukkit.getConsoleSender());
        }
    }

}
