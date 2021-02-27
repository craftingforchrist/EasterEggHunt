package net.craftingforchrist.EasterEggHunt;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Egg;
import org.bukkit.plugin.Plugin;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EggHolographicLeaderboard {
    public static EasterEggHuntMain plugin;
    public EggHolographicLeaderboard(EasterEggHuntMain instance) {
        plugin = instance;
    }

    public static void HoloLeaderDisplay() {
        System.out.println(plugin.getConfig().getInt("EGG.LEADERBOARD.X"));
        int HoloLeaderboardx = plugin.getConfig().getInt("EGG.LEADERBOARD.X");
        int HoloLeaderboardy = plugin.getConfig().getInt("EGG.LEADERBOARD.Y");
        int HoloLeaderboardz = plugin.getConfig().getInt("EGG.LEADERBOARD.Z");

        Location HoloLeaderboardLocation = new Location(Bukkit.getWorld("world"), HoloLeaderboardx, HoloLeaderboardy, HoloLeaderboardz);
        Hologram hologram = HologramsAPI.createHologram(plugin, HoloLeaderboardLocation);

        //
        // Database Query
        // Check how many eggs the player has collected.
        //
        try {
            PreparedStatement findstatement = plugin.getConnection().prepareStatement("select (select username from playerdata) as 'username', count(*) as 'eastereggs' from eastereggs;");

            ResultSet results = findstatement.executeQuery();
            if (results.next()) {

                TextLine textLine1 = hologram.appendTextLine("...");
                TextLine textLine2 = hologram.insertTextLine(0, "...");

            }
        } catch (SQLException e) {
            e.printStackTrace();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONERROR")));
        }
    }
}
