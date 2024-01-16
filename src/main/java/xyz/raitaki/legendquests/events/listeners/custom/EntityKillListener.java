package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerKillEntityEvent;
import xyz.raitaki.legendquests.events.PlayerQuestKillEvent;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class EntityKillListener implements Listener {

  @EventHandler
  public void onEntityKill(PlayerKillEntityEvent event) {
    Player player = event.getPlayer();
    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(player);
    PlayerQuest quest = questPlayer.getPlayerQuestByCheckpointType(CheckPointTypeEnum.KILL);

    if (quest == null) {
      return;
    }

    String entityName = event.getEntity().getName();
    String questEntityName = quest.getCheckPoint().getValue();

    if (!entityName.equals(questEntityName)) {
      return;
    }

    PlayerQuestKillEvent playerQuestKillEvent = new PlayerQuestKillEvent(quest, questPlayer,
        event.getEntity());
    Bukkit.getPluginManager().callEvent(playerQuestKillEvent);
  }
}
