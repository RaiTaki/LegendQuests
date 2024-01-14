package xyz.raitaki.legendquests.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

  public static String itemToString(ItemStack item) {
    YamlConfiguration config = new YamlConfiguration();
    config.set("Item", item);
    return config.saveToString();
  }

  public static ItemStack stringToItem(String string) {
    try {
      YamlConfiguration config = new YamlConfiguration();
      config.loadFromString(string);
      return config.getItemStack("Item");
    } catch (InvalidConfigurationException e) {
      throw new IllegalArgumentException("String is not an item: " + string);
    }
  }
}
