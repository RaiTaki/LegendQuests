package xyz.raitaki.legendquests.utils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Display.TextDisplay;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {

  /**
   * get the add entity packet
   * @param as the text display
   * @return the packet
   */
  public static ClientboundAddEntityPacket getAddEntityPacket(TextDisplay as) {
    return new ClientboundAddEntityPacket(as);
  }

  /**
   * get the set entity data packet
   * @param textDisplay the text display
   * @return the packet
   */
  public static ClientboundSetEntityDataPacket getSetEntityDataPacket(TextDisplay textDisplay) {
    return new ClientboundSetEntityDataPacket(textDisplay.getId(), textDisplay.getEntityData().getNonDefaultValues());
  }

  /**
   * get the remove entity packet
   * @param textDisplay the text display
   * @return the packet
   */
  public static ClientboundRemoveEntitiesPacket getRemoveEntityPacket(TextDisplay textDisplay) {
    return new ClientboundRemoveEntitiesPacket(textDisplay.getId());
  }

  /**
   * get the teleport entity packet
   * @param textDisplay the text display
   * @return the packet
   */
  public static ClientboundTeleportEntityPacket getTeleportEntityPacket(TextDisplay textDisplay) {
    return new ClientboundTeleportEntityPacket(textDisplay);
  }

  /**
   * send a packet to a player
   * @param p the player to send the packet to
   * @param packet the packet to send
   */
  public static void sendPacket(Player p, Packet packet) {
    ((CraftPlayer) p).getHandle().connection.send(packet);
  }
}