package me.anfanik.steda.api.sidebar;

import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SidebarImpl implements Sidebar {

    @Getter
    private final String name;
    private final Map<UUID, Scoreboard> scoreboards = new ConcurrentHashMap<>();
    @Getter
    private final Row[] rows;
    @Getter
    private Function<Player, String> titleGenerator = player -> "";

    public SidebarImpl(String name, Row[] rows) {
        this.name = name;
        this.rows = rows;
        for (Row row : rows) {
            row.addUpdateCallback((player, text) -> {
                Scoreboard scoreboard = scoreboards.get(player.getUniqueId());
                Team team = scoreboard.getTeam(String.format("%s.%s", name, row.getId()));
                team.setPrefix(text);
            });
        }
    }

    private static ChatColor parseColor(char[] chars) {
        for (ChatColor color : ChatColor.values()) {
            char[] colorChars = color.toString().toCharArray();

            int same = 0;
            for (int i = 0; i < 2; i++) {
                if (colorChars[i] == chars[i])
                    same++;
            }

            if (same == 2) {
                return color;
            }
        }

        return null;
    }

    public String[] splitText(String text) {
        String[] parts = new String[2];

        ChatColor firstColor = ChatColor.WHITE, secondColor = null;
        Character previousSymbol = null;

        parts[0] = "";
        for (int index = 0; index < text.length() / 2; index++) {
            char symbol = text.charAt(index);

            if (previousSymbol != null) {
                ChatColor color = parseColor(new char[]{previousSymbol, symbol});

                if (color != null) {
                    secondColor = color;
                    firstColor = color;
                }
            }

            parts[0] += symbol;
            previousSymbol = symbol;
        }

        parts[1] = firstColor + "" + (secondColor != null ? secondColor : "") + text.substring(text.length() / 2);
        return parts;
    }

    private Scoreboard getScoreboard(Player player) {
        if (scoreboards.containsKey(player.getUniqueId())) {
            return scoreboards.get(player.getUniqueId());
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(TextUtility.colorize(titleGenerator.apply(player)));
        scoreboards.put(player.getUniqueId(), scoreboard);

        for (int index = 0; index < rows.length; index++) {
            Row row = rows[index];
            Team team = scoreboard.registerNewTeam(String.format("%s.%s", name, row.getId()));
            net.md_5.bungee.api.ChatColor chatColor = ChatColor.values()[index];
            team.addEntry(chatColor.toString());
            objective.getScore(chatColor.toString()).setScore(15 - index);
            row.update(player);
        }
        return scoreboard;
    }

    public void setTitle(Function<Player, String> titleGenerator) {
        this.titleGenerator = titleGenerator;
        scoreboards.forEach((uuid, scoreboard) -> {
            Objective objective = scoreboard.getObjective(name);
            Player player = Bukkit.getPlayer(uuid);
            objective.setDisplayName(titleGenerator.apply(player));
        });
    }

    @Override
    public void show(Player player) {
        player.setScoreboard(getScoreboard(player));
    }

    @Override
    public void hide(Player player) {
        scoreboards.remove(player.getUniqueId());
    }

}