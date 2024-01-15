package xyz.raitaki.legendquests.utils;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.decoration.ArmorStand;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {

  public static ClientboundAddEntityPacket getAddEntityPacket(TextDisplay as){
    return new ClientboundAddEntityPacket(as);
  }

  public static ClientboundSetEntityDataPacket getSetEntityDataPacket(TextDisplay as){
    return new ClientboundSetEntityDataPacket(as.getId(), as.getEntityData().packDirty());
  }

  public static ClientboundRemoveEntitiesPacket getRemoveEntityPacket(TextDisplay as){
    return new ClientboundRemoveEntitiesPacket(as.getId());
  }

  public static ClientboundTeleportEntityPacket getTeleportEntityPacket(TextDisplay as){
    return new ClientboundTeleportEntityPacket(as);
  }

  public static void sendPacket(Player p, Packet packet){
    ((CraftPlayer) p).getHandle().connection.send(packet);
  }
}