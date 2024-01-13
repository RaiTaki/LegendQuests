package xyz.raitaki.legendquests.utils;

import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;

public class ItemUtils {

    public static String itemToString(ItemStack item){
        YamlConfiguration config = new YamlConfiguration();
        config.set("Item", item);
        return config.saveToString();
    }

    public static ItemStack stringToItem(String string){
        try {
            YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(string);
            return config.getItemStack("Item");
        }
        catch(InvalidConfigurationException e){
            throw new IllegalArgumentException("String is not an item: " + string);
        }
    }
}
