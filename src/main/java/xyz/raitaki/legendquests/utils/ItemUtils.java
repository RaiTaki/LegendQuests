package xyz.raitaki.legendquests.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

  /**
   * Serialize an item to a string
   * @param item the item to serialize
   * @return the serialized item as a string
   */
  public static String itemToString(ItemStack item) {
    YamlConfiguration config = new YamlConfiguration();
    config.set("Item", item);
    return config.saveToString();
  }

  /**
   * Deserialize an item from a string
   * @param string the string to deserialize
   * @return the deserialized item
   */
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
