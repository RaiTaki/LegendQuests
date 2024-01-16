package xyz.raitaki.legendquests.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public class PacketDisplay {

  private Player player;
  private TextDisplay textDisplay;
  private String text;
  private String defaultText;
  private Location location;
  private int lineWidth = 20;
  private boolean shadow = true;
  private TextAlignment alignment = TextAlignment.CENTER;

  public PacketDisplay(Player player, String text) {
    this.text = text;
    this.defaultText = text;
    this.player = player;
    //location = SettingsConfig.getInstance().getLocation("questtracker.location");
    location = SettingsConfig.getInstance().getLocationValue("questtracker.location");

    if (location == null) {
      throw new NullPointerException("Location is null on 'questtracker.location'");
    }

    textDisplay = new TextDisplay(EntityType.TEXT_DISPLAY,
        ((CraftWorld) location.getWorld()).getHandle());
    textDisplay.teleportTo(location.getX(), location.getY(), location.getZ());
    textDisplay.setText(Component.literal(text));
    textDisplay.setBillboardConstraints(BillboardConstraints.FIXED);
    sendDataPacket(true);
    setRotation();
  }

  public void setText(String text) {
    this.text = text;
    textDisplay.setText(
        Component.literal(text).withStyle(Style.EMPTY.withColor(TextColor.parseColor("gray"))));

    sendDataPacket(false);
  }

  public void sendDataPacket(boolean spawn) {
    if (spawn) {
      sendSpawnPacket();
    }

    ClientboundSetEntityDataPacket dataPacket = PacketUtils.getSetEntityDataPacket(textDisplay);
    PacketUtils.sendPacket(player, dataPacket);
  }

  public void sendSpawnPacket() {
    Packet<ClientGamePacketListener> packet = textDisplay.getAddEntityPacket();
    PacketUtils.sendPacket(player, packet);
  }

  public void sendTeleportPacket() {
    Packet<ClientGamePacketListener> packet = PacketUtils.getTeleportEntityPacket(textDisplay);
    PacketUtils.sendPacket(player, packet);
  }

  public void setRotation() {
    textDisplay.setRot(location.getYaw(), location.getPitch());
    sendTeleportPacket();
  }

  public void updateText() {
    String[] lines = defaultText.split("\n");
    StringBuilder text = new StringBuilder();
    for (String line : lines) {
      text.append(PlaceholderAPI.setPlaceholders(player, line)).append("\n");
    }

    setText(text.toString());
  }

  public void setLocation(Location location) {
    this.location = location;
    textDisplay.teleportTo(location.getX(), location.getY(), location.getZ());
    textDisplay.setRot(location.getYaw(), location.getPitch());
    Bukkit.broadcastMessage("Location: " + location.toString());
    sendTeleportPacket();
  }

  public static void updateDisplayLocation() {
    Location loc = SettingsConfig.getInstance().getLocationValue("questtracker.location");
    if (loc == null) {
      return;
    }
    for (QuestPlayer questPlayer : QuestManager.getQuestPlayers().values()) {
      PacketDisplay packetDisplay = questPlayer.getPacketDisplay();
      if (packetDisplay != null) {
        questPlayer.getPacketDisplay().setLocation(loc);
      }
    }
  }

  public static void startUpdateTimer() {
    new BukkitRunnable() {
      @Override
      public void run() {
        for (QuestPlayer questPlayer : QuestManager.getQuestPlayers().values()) {
          PacketDisplay packetDisplay = questPlayer.getPacketDisplay();
          if (packetDisplay != null) {
            packetDisplay.updateText();
          }
        }
      }
    }.runTaskTimerAsynchronously(LegendQuests.getInstance(), 0, 20);
  }

}
