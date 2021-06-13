package me.anfanik.steda.api;

import lombok.val;
import me.anfanik.sharkly.utility.Formatter;
import me.anfanik.steda.api.chat.ChatInputService;
import me.anfanik.steda.api.command.exact.PingCommand;
import me.anfanik.steda.api.menu.Menus;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class StedaApi {

    static void initialize(Plugin plugin) {
        val pluginManager = Bukkit.getPluginManager();
        Menus.initialize(plugin);
        new ChatInputService(plugin, pluginManager);
        new PingCommand();

        if (pluginManager.getPlugin("Sharkly") != null) {
            plugin.getLogger().info("Initializing a integration with the Sharkly.");
            Formatter.addFormatter(TextUtility::colorize);
        }
    }

}
