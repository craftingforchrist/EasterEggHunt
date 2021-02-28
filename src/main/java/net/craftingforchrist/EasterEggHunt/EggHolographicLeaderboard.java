package net.craftingforchrist.EasterEggHunt;

public class EggHolographicLeaderboard {
    public static EasterEggHuntMain plugin;
    public EggHolographicLeaderboard(EasterEggHuntMain instance) {
        plugin = instance;
    }

    public static void HoloLeaderDisplay() {
        String dbname = plugin.getConfig().getString("DATABASE.DATABASE");


    }
}
