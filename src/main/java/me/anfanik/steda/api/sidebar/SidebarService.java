package me.anfanik.steda.api.sidebar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SidebarService implements Listener {

    private static SidebarService instance;

    public static SidebarService get() {
        if (instance == null) {
            instance = new SidebarService();
        }
        return instance;
    }

    private static final Scoreboard EMPTY_SCOREBOARD = Bukkit.getScoreboardManager().getMainScoreboard();

    private final Map<UUID, Sidebar> sidebars = new HashMap<>();

    public Sidebar get(Player player) {
        return sidebars.get(player);
    }

    public void show(Player player, Sidebar sidebar) {
        sidebar.show(player);
        sidebars.put(player.getUniqueId(), sidebar);
    }

    public void hide(Player player) {
        UUID uuid = player.getUniqueId();
        Sidebar sidebar = sidebars.remove(uuid);
        if (sidebar != null) {
            sidebar.hide(player);
        }
        player.setScoreboard(EMPTY_SCOREBOARD);
    }

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        hide(player);
    }

}
