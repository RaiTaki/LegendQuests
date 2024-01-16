package xyz.raitaki.legendquests.events.listeners.vanilla;

import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditGuiTypeEnum.CHECKPOINT;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditGuiTypeEnum.REWARD;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditGuiTypeEnum.TRACKER;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
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

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    QuestManager.addBaseQuestToPlayer(event.getPlayer(), QuestManager.getBaseQuests().get(0));
    QuestManager.getQuestPlayerByPlayer(event.getPlayer()).sendQuestInfoChat();

    PacketDisplay packetDisplay = new PacketDisplay(event.getPlayer(),
        "%legendquest_questname%\n%legendquest_questdescription%\n%legendquest_checkpoint%\n%legendquest_remainingtime%");

    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(event.getPlayer());
    questPlayer.setPacketDisplay(packetDisplay);
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
    } else if (quest.getQuestGUI().getEditGuiType() == CHECKPOINT) {
      CheckpointGui checkpointGUI = quest.getQuestGUI().getEditedCheckpoint();
      if (checkpointGUI == null) {
        return;
      }
      checkpointGUI.setChatMessage(text);
    } else if (quest.getQuestGUI().getEditGuiType() == TRACKER) {
      quest.getQuestGUI().doTrackerText(text);
    }
    event.setCancelled(true);
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
    Player player = event.getPlayer();
    Entity entity = event.getRightClicked();
    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(player);
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
    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(player);
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
