package xyz.raitaki.legendquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerKillEntityEvent;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class EntityKillListener implements Listener {

    @EventHandler
    public void onEntityKill(PlayerKillEntityEvent event){
        Player player = event.getPlayer();
        QuestPlayer questPlayer = QuestManager.getQuestPlayerFromPlayer(player);
        PlayerQuest quest = questPlayer.getPlayerQuestByCheckpointType(QuestCheckpoint.CheckPointType.KILL);

        //TODO: Check if entities are the same
    }
}
