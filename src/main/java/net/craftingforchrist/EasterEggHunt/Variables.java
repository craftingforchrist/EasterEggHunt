package net.craftingforchrist.EasterEggHunt;

public class Variables {
    private static EasterEggHuntMain plugin;

    public static String DATABASEHOST;
    public static String DATABASEPORT;
    public static String DATABASEDATABASE;
    public static String DATABASEUSERNAME;
    public static String DATABASEPASSWORD;


    public void Variables(EasterEggHuntMain plugin) {
        this.plugin = plugin;

        // Database
        DATABASEHOST = plugin.getConfig().getString("DATABASE.HOST");
        DATABASEPORT = plugin.getConfig().getString("DATABASE.PORT");
        DATABASEDATABASE = plugin.getConfig().getString("DATABASE.DATABASE");
        DATABASEUSERNAME = plugin.getConfig().getString("DATABASE.USERNAME");
        DATABASEPASSWORD = plugin.getConfig().getString("DATABASE.PASSWORD");
    }
}
