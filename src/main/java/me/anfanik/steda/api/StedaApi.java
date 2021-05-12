package me.anfanik.steda.api;

import me.anfanik.sharkly.utility.Formatter;
import me.anfanik.steda.api.chat.ChatInputService;
import me.anfanik.steda.api.command.exact.PingCommand;
import me.anfanik.steda.api.menu.MenuService;
import me.anfanik.steda.api.sidebar.SidebarService;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class StedaApi {

    static void initialize(Plugin plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        new MenuService(plugin, pluginManager);
        new ChatInputService(plugin, pluginManager);
        new PingCommand();

        if (pluginManager.getPlugin("Sharkly") != null) {
            plugin.getLogger().info("Initializing a integration with Sharkly.");
            Formatter.addFormatter(TextUtility::colorize);
        }
    }

}
