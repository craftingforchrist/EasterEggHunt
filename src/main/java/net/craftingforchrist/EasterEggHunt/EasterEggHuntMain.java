package net.craftingforchrist.EasterEggHunt;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import net.craftingforchrist.EasterEggHunt.commands.clearegg;
import net.craftingforchrist.EasterEggHunt.commands.egg;
import net.craftingforchrist.EasterEggHunt.events.EggFindEvent;
import net.craftingforchrist.EasterEggHunt.events.EggHunterOnJoin;
import net.craftingforchrist.EasterEggHunt.events.EggMilestoneReachedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.*;

public class EasterEggHuntMain extends JavaPlugin {
    public static EasterEggHuntMain plugin;
    private Connection connection;
    private boolean useHolographicDisplays;

    @Override
    public void onEnable() {
        plugin = this;

        plugin.saveDefaultConfig(); // Generate configuration file
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        establishConnection(); // Connect to the database

        // Plugin Load Message
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "\n\n" + plugin.getDescription().getName() + " is now enabled.\nRunning Version: " + plugin.getDescription().getVersion() + "\nGitHub Repository: https://github.com/craftingforchrist/EasterEggHunt\nCreated By: " + plugin.getDescription().getAuthors() + "\n\n");

        // Plugin Event Register
        PluginManager pluginmanager = plugin.getServer().getPluginManager();
        pluginmanager.registerEvents(new EggFindEvent(this), this);
        pluginmanager.registerEvents(new EggHunterOnJoin(this), this);
        pluginmanager.registerEvents(new EggMilestoneReachedEvent(this), this);

        // Command Registry
        this.getCommand("egg").setExecutor(new egg(this));
        this.getCommand("clearegg").setExecutor(new clearegg(this));










        int HoloLeaderboardx = plugin.getConfig().getInt("EGG.LEADERBOARD.X");
        int HoloLeaderboardy = plugin.getConfig().getInt("EGG.LEADERBOARD.Y");
        int HoloLeaderboardz = plugin.getConfig().getInt("EGG.LEADERBOARD.Z");

        Location HoloLeaderboardLocation = new Location(Bukkit.getWorld("world"), HoloLeaderboardx, HoloLeaderboardy, HoloLeaderboardz);
        Hologram Hologram = HologramsAPI.createHologram(plugin, HoloLeaderboardLocation);

        TextLine BoardTitle = Hologram.insertTextLine(0, ChatColor.translateAlternateColorCodes('&', "&e&lEgg Hunter Leaderboard"));
        TextLine firstplace = Hologram.insertTextLine(1, "1st Place: ");
        TextLine secondplace = Hologram.insertTextLine(2, "2nd Place: ");
        TextLine thirdplace = Hologram.insertTextLine(3, "3rd Place: ");

        BukkitScheduler scheduler = plugin.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                //
                // Database Query
                // Check how many eggs the player has collected.
                //
                try {
                    PreparedStatement findstatement = plugin.getConnection().prepareStatement("SELECT playerdata.username as 'username', COUNT(*) as 'eggs' FROM eastereggs left join playerdata on playerdata.id = eastereggs.playerid group by playerdata.username LIMIT 3;", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

                    ResultSet results = findstatement.executeQuery();

                    if (results.next()) {
                        int rowCount = results.last() ? results.getRow() : 0;
                        String username = results.getString(1);
                        String eggs = results.getString(2);

//                        while(results.next()) {
//                            for(int i = 1; i<=rowCount; i++) {
//
//                            }
//                        }

                        do{
                            Bukkit.getPlayer("shadowolfyt").sendMessage(results.getString(1)+","+results.getString(2));
                        } while(results.next());


                        firstplace.setText("1st Place: " + username + " :: " + eggs);
                        secondplace.setText("2nd Place: " + username + " :: " + eggs);
                        thirdplace.setText("3rd Place: " + username + " :: " + eggs);

                        results.next();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONERROR")));
                }
            }
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        // Plugin Shutdown Message
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n\n" + plugin.getDescription().getName() + " is now disabled.\n\n");
    }

    public void establishConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            plugin.connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getConfig().getString("DATABASE.HOST") + ":" + plugin.getConfig().getString("DATABASE.PORT") + "/" + plugin.getConfig().getString("DATABASE.DATABASE"), plugin.getConfig().getString("DATABASE.USERNAME"), plugin.getConfig().getString("DATABASE.PASSWORD"));
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONSUCCESS")));
        } catch (SQLException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONERROR")));

//            getLogger().severe("*** A MySQL database is required for the function of this plugin. ***");
//            getLogger().severe("*** This plugin will be disabled. ***");
//            this.setEnabled(false);

            e.printStackTrace();
            return;
        } catch (ClassNotFoundException e) {
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONERROR")));
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        if (plugin.connection == null) {
            establishConnection();
        } else {
            try {
                plugin.connection.close();
            } catch (SQLException e) {
                plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LANG.DATABASE.CONNECTIONERROR")));
                e.printStackTrace();
            }
            establishConnection();
        }
        return connection;
    }

}
