package xyz.raitaki.legendquests.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.raitaki.legendquests.LegendQuests;

import static org.bukkit.Bukkit.getServer;

public class EconomyUtils {

    private static Economy econ = null;

    public static void setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            Bukkit.getLogger().severe("Vault not found!");
            Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            Bukkit.getLogger().severe("Economy not found!");
            Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
            return;
        }
        econ = rsp.getProvider();
    }

    public static void giveMoney(Player player, double amount){
        econ.depositPlayer(player, amount);
    }
}
