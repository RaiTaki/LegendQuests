package xyz.raitaki.legendquests.utils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Level;

public class ItemUtils {

    public static String seriliseItemStack(ItemStack item) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(1);
            dataOutput.writeObject(item);

            return Base64Coder.encodeLines(outputStream.toByteArray());

        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to serilise itemstack \n" + exception);
            return "";
        }
    }

    public static ItemStack deseriliseItemStack(String source) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(source));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack item = (ItemStack) dataInput.readObject();

            return item;
        } catch (Exception exception) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to deserilise itemstack \n" + exception);
            return null;
        }
    }
}
