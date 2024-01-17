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
import xyz.raitaki.legendquests.database.DatabaseConnection;
import xyz.raitaki.legendquests.database.objects.QuestData;
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

public class PlayerEventListener implements Listener {

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    DatabaseConnection.getPlayerData(player.getUniqueId().toString())
        .thenAccept(playerData -> {

          for (QuestData questData : playerData.getQuests()) {
            //QuestManager.loadPlayerQuestFromData(event.getPlayer(), questData);
            QuestManager.loadPlayerQuestFromData(player, questData);
          }
          QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(player);
          questPlayer.sendQuestInfoChat();
        });
  }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(event.getPlayer());
    DatabaseConnection.savePlayerData(questPlayer);
    QuestManager.removeQuestPlayer(event.getPlayer());
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
        CheckPointTypeEnum.INTERACT);

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
