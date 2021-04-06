package me.anfanik.steda.api.chat;

import me.anfanik.steda.api.utility.cache.Cache;
import me.anfanik.steda.api.utility.cache.HashCache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * @author Anfanik
 * Date: 06/10/2019
 */

public class ChatInputService implements Listener {

    private static ChatInputService instance; {
        instance = this;
    }

    public static ChatInputService get() {
        return instance;
    }

    private final Plugin plugin;

    public ChatInputService(Plugin plugin, PluginManager pluginManager) {
        this.plugin = plugin;
        pluginManager.registerEvents(this, plugin);
    }

    private final Cache<Player, BiConsumer<Player, String>> cache = new HashCache<>(1, TimeUnit.MINUTES);

    public void addInputListener(Player player, BiConsumer<Player, String> messageConsumer) {
        cache.put(player, messageConsumer);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void handleChatMessage(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (cache.containsKey(player)) {
            String message = event.getMessage();
            if (!message.equalsIgnoreCase("отмена") && !message.equalsIgnoreCase("cancel")) {
                BiConsumer<Player, String> messageConsumer = cache.get(player);
                Bukkit.getScheduler().runTask(plugin, () -> messageConsumer.accept(player, message));
            }
            cache.remove(player);
            event.setCancelled(true);
        }
    }

}
