package me.anfanik.steda.api.sidebar;

import lombok.Getter;
import me.anfanik.steda.api.utility.TextUtility;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Anfanik
 * Date: 10/12/2019
 */

public class SidebarImpl implements Sidebar {

    @Getter
    private final String name;

    @Getter
    private Function<Player, String> titleGenerator = player -> "";

    private final Map<Player, Scoreboard> scoreboards = new ConcurrentHashMap<>();

    @Getter
    private Row[] rows;

    public SidebarImpl(String name, Row[] rows) {
        this.name = name;
        this.rows = rows;
        for (int index = 0; index < rows.length; index++) {
            Row row = rows[index];
            row.addUpdateCallback((player, text) -> {
                Scoreboard scoreboard = scoreboards.get(player);
                Team team = scoreboard.getTeam(String.format("%s.%s", name, row.getId()));
                team.setPrefix(text);
                //String[] parts = splitText(text);
                //team.setPrefix(parts[0]);
                //team.setPrefix(parts[1]);
            });
        }
    }

    public String[] splitText(String text) {
        String[] parts = new String[2];

        ChatColor firstColor = ChatColor.WHITE, secondColor = null;
        Character previousSymbol = null;

        parts[0] = "";
        for(int index = 0; index < text.length() / 2; index++){
            char symbol = text.charAt(index);

            if(previousSymbol != null){
                ChatColor color = parseColor(new char[] {previousSymbol, symbol});

                if(color != null){
                    if(color.isFormat()) {
                        secondColor = color;
                    } else {
                        firstColor = color;
                        secondColor = null;
                    }
                }
            }

            parts[0] += symbol;
            previousSymbol = symbol;
        }

        parts[1] = firstColor + "" + (secondColor != null ? secondColor : "") + text.substring(text.length() / 2);
        return parts;
    }

    private static ChatColor parseColor(char[] chars){
        for (ChatColor color : ChatColor.values()){
            char[] colorChars = color.toString().toCharArray();

            int same = 0;
            for(int i = 0; i < 2; i++){
                if(colorChars[i] == chars[i])
                    same++;
            }

            if (same == 2) {
                return color;
            }
        }

        return null;
    }

    private Scoreboard getScoreboard(Player player) {
        if (scoreboards.containsKey(player)) {
            return scoreboards.get(player);
        }

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(name, "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(TextUtility.colorize(titleGenerator.apply(player)));
        scoreboards.put(player, scoreboard);

        for (int index = 0; index < rows.length; index++) {
            Row row = rows[index];
            Team team = scoreboard.registerNewTeam(String.format("%s.%s", name, row.getId()));
            ChatColor chatColor = ChatColor.values()[index];
            team.addEntry(chatColor.toString());
            objective.getScore(chatColor.toString()).setScore(15 - index);
            row.update(player);
        }
        return scoreboard;
    }

    public void setTitle(Function<Player, String> titleGenerator) {
        this.titleGenerator = titleGenerator;
        scoreboards.forEach((player, scoreboard) -> {
            Objective objective = scoreboard.getObjective(name);
            System.out.println("SCOREBOARD:" + scoreboard);
            System.out.println("OBJCTVS:" + scoreboard.getObjectives());
            System.out.println("TITLE FOR PLAYER: " + player);
            System.out.println("SETTING TITLE: " + titleGenerator.apply(player));
            System.out.println("OBJ: " + objective);
            objective.setDisplayName(titleGenerator.apply(player));
        });
    }

    @Override
    public void show(Player player) {
        player.setScoreboard(getScoreboard(player));
    }

    @Override
    public void hide(Player player) {
        scoreboards.remove(player);
    }

}