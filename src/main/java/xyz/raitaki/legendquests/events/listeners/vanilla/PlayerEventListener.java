package xyz.raitaki.legendquests.events.listeners.vanilla;

import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditGuiTypeEnum.CHECKPOINT;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditGuiTypeEnum.REWARD;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display.BillboardConstraints;
import net.minecraft.world.entity.Display.TextDisplay;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay.TextAlignment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import xyz.raitaki.legendquests.events.PlayerQuestInteractEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.gui.CheckpointGui;
import xyz.raitaki.legendquests.questhandlers.gui.RewardGui;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerInteractionCheckpoint;
import xyz.raitaki.legendquests.utils.PacketDisplay;
import xyz.raitaki.legendquests.utils.PacketUtils;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    QuestManager.addBaseQuestToPlayer(event.getPlayer(), QuestManager.getBaseQuests().get(0));
    QuestManager.getBaseQuests().getFirst().showGui(event.getPlayer());
    QuestManager.getQuestPlayerFromPlayer(event.getPlayer()).sendQuestInfoChat();

    World world = event.getPlayer().getWorld();
    ServerLevel level = ((CraftWorld) world).getHandle();

    SettingsConfig.getInstance().setLocation("questtracker.location", event.getPlayer().getLocation());

    PacketDisplay packetDisplay = new PacketDisplay(event.getPlayer(), "Test");
    packetDisplay.setText("Test2\nASDASDASD\nAASDASDwwss");

    //packetDisplay.setAlignment(TextAlignment.LEFT);

  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {

  }

  @EventHandler
  public void onAsyncChat(AsyncChatEvent event) {
    Player player = event.getPlayer();
    TextComponent message = (TextComponent) event.message();
    String text = message.content();
    QuestBase quest = QuestManager.getQuestBaseFromEditor(player);
      if (quest == null) {
          return;
      }
      if (quest.getQuestGUI().getEditGuiType() == null) {
          return;
      }
    if (quest.getQuestGUI().getEditGuiType() == REWARD) {
      RewardGui rewardGUI = quest.getQuestGUI().getEditedReward();
        if (rewardGUI == null) {
            return;
        }
      rewardGUI.setChatMessage(text);
    }
    if (quest.getQuestGUI().getEditGuiType() == CHECKPOINT) {
      CheckpointGui checkpointGUI = quest.getQuestGUI().getEditedCheckpoint();
        if (checkpointGUI == null) {
            return;
        }
      checkpointGUI.setChatMessage(text);
    }
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();
    QuestPlayer questPlayer = QuestManager.getQuestPlayerFromPlayer(player);
    PlayerQuest playerQuest = questPlayer.getPlayerQuestByCheckpointType(
        CheckPointTypeEnum.INTERECT);

      if (playerQuest == null) {
          return;
      }

    PlayerCheckpoint playerCheckpoint = playerQuest.getCheckPoint();
    String entityName = entity.getName();
      if (!(playerCheckpoint instanceof PlayerInteractionCheckpoint interactionCheckpoint)) {
          return;
      }

    String questEntityName = interactionCheckpoint.getNpcName();
      if (!entityName.equals(questEntityName)) {
          return;
      }

    PlayerQuestInteractEvent playerQuestInteractEvent = new PlayerQuestInteractEvent(playerQuest,
        questPlayer, entity);
    Bukkit.getPluginManager().callEvent(playerQuestInteractEvent);
  }

  @EventHandler
  public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
    Player player = event.getPlayer();
    QuestPlayer questPlayer = QuestManager.getQuestPlayerFromPlayer(player);
    PlayerQuest playerQuest = questPlayer.getPlayerQuestByCheckpointType(
        CheckPointTypeEnum.CONVERSATION);

      if (playerQuest == null) {
          return;
      }
    PlayerConversationCheckpoint playerCheckpoint = (PlayerConversationCheckpoint) playerQuest.getCheckPoint();
      if (playerCheckpoint == null) {
          return;
      }

    if (player.isSneaking()) {
      playerCheckpoint.doAnswer();
    } else {
      playerCheckpoint.changeAnswer();
    }

    event.setCancelled(true);
  }
}
