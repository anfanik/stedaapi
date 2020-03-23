package me.anfanik.steda.api;

import me.anfanik.steda.api.chat.ChatInputService;
import me.anfanik.steda.api.command.exact.PingCommand;
import me.anfanik.steda.api.menu.MenuService;
import me.anfanik.steda.api.sidebar.SidebarService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 * @author Anfanik
 * Date: 17/09/2019
 */

public class StedaApi {

    static void initialize(Plugin plugin) {
        PluginManager pluginManager = Bukkit.getPluginManager();
        new MenuService(plugin, pluginManager);
        new ChatInputService(plugin, pluginManager);
        new SidebarService();
        new PingCommand();
    }

}
