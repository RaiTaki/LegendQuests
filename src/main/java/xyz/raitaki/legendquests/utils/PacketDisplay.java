package xyz.raitaki.legendquests.utils;

import com.mojang.serialization.Codec;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.util.CraftMagicNumbers.NBT;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay.TextAlignment;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public class PacketDisplay {

  private Player player;
  private TextDisplay textDisplay;
  private String text;
  private Location location;
  private int lineWidth = 20;
  private boolean shadow = true;
  private TextAlignment alignment = TextAlignment.CENTER;

  public PacketDisplay(Player player, String text){
    this.text = text;
    this.player = player;
    //location = SettingsConfig.getInstance().getLocation("questtracker.location");
    location = player.getLocation();

    if(location == null){
      throw new NullPointerException("Location is null on 'questtracker.location'");
    }

    textDisplay = new TextDisplay(EntityType.TEXT_DISPLAY, ((CraftWorld) player.getWorld()).getHandle());
    textDisplay.teleportTo(location.getX(), location.getY(), location.getZ());
    textDisplay.setText(Component.empty().append(text));
    textDisplay.setBillboardConstraints(BillboardConstraints.FIXED);
    sendDataPacket(true);
    setRotation();
  }

  public void setText(String text){
    this.text = text;
    textDisplay.setText(Component.empty().append(text));
    sendDataPacket(false);

    Bukkit.broadcastMessage("flags: " + textDisplay.getFlags());
  }

  public void sendDataPacket(boolean spawn){
    if(spawn) sendSpawnPacket();

    ClientboundSetEntityDataPacket dataPacket = PacketUtils.getSetEntityDataPacket(textDisplay);
    ((CraftPlayer) player).getHandle().connection.send(dataPacket);
  }

  public void sendSpawnPacket(){
    Packet<ClientGamePacketListener> packet = textDisplay.getAddEntityPacket();
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

  public void sendTeleportPacket(){
    Packet<ClientGamePacketListener> packet = PacketUtils.getTeleportEntityPacket(textDisplay);
    ((CraftPlayer) player).getHandle().connection.send(packet);
  }

  public void setRotation(){
    textDisplay.setRot(player.getLocation().getYaw(), -player.getLocation().getPitch());
    sendTeleportPacket();
  }

}
