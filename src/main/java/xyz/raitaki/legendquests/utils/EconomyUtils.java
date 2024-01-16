package xyz.raitaki.legendquests.utils;

import static org.bukkit.Bukkit.getServer;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import xyz.raitaki.legendquests.LegendQuests;

public class EconomyUtils {

  private static Economy econ = null;

  /**
   * setup the economy
   */
  public static void setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      Bukkit.getLogger().severe("Vault not found!");
      Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
      return;
    }
    RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
        .getRegistration(Economy.class);
    if (rsp == null) {
      Bukkit.getLogger().severe("Economy not found!");
      Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
      return;
    }
    econ = rsp.getProvider();
  }

  /**
   * add money to a player
   * @param player the player to check
   * @param amount the amount to give
   */
  public static void giveMoney(Player player, double amount) {
    econ.depositPlayer(player, amount);
  }
}
