package xyz.raitaki.legendquests.events.listeners.vanilla;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import xyz.raitaki.legendquests.events.PlayerQuestInteractEvent;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerInteractionCheckpoint;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        QuestPlayer questPlayer = QuestManager.getQuestPlayerFromPlayer(player);
        PlayerQuest playerQuest = questPlayer.getPlayerQuestByCheckpointType(QuestCheckpoint.CheckPointType.INTERECT);

        if(playerQuest == null) return;

        PlayerCheckpoint playerCheckpoint = playerQuest.getCheckPoint();
        String entityName = entity.getName();
        if(!(playerCheckpoint instanceof PlayerInteractionCheckpoint interactionCheckpoint)) return;

        String questEntityName = interactionCheckpoint.getNpcName();
        if(!entityName.equals(questEntityName)) return;

        PlayerQuestInteractEvent playerQuestInteractEvent = new PlayerQuestInteractEvent(playerQuest, questPlayer, entity);
        Bukkit.getPluginManager().callEvent(playerQuestInteractEvent);
    }
}
