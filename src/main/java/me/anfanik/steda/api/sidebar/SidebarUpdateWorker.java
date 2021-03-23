package me.anfanik.steda.api.sidebar;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public abstract class SidebarUpdateWorker implements Listener {

    @Getter
    protected final Sidebar sidebar;

    public SidebarUpdateWorker(Plugin plugin, Sidebar sidebar) {
        this.sidebar = sidebar;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getOnlinePlayers().forEach(player -> SidebarService.get().show(player, sidebar));
    }

    @EventHandler(priority = EventPriority.LOW)
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SidebarService.get().show(player, sidebar);
    }

    public void invalidate() {
        HandlerList.unregisterAll(this);
        Bukkit.getOnlinePlayers().forEach(SidebarService.get()::hide);
    }

}

