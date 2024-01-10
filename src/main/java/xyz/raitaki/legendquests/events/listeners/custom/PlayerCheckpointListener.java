package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerInteractionCheckpoint;

public class PlayerCheckpointListener implements Listener {

    @EventHandler
    public void onCheckpointUpdate(PlayerNextCheckpointEvent event){
        PlayerQuest quest = event.getPlayerQuest();
        QuestPlayer player = event.getQuestPlayer();
        PlayerCheckpoint currentCheckpoint = event.getCurrentCheckpoint();
        PlayerCheckpoint nextCheckpoint = event.getNextCheckpoint();

        player.getPlayer().sendMessage("§cYou have completed the checkpoint " + currentCheckpoint.getType().name() + " for the quest " + quest.getQuest().getName());
        player.getPlayer().sendMessage("");

        if(nextCheckpoint instanceof PlayerConversationCheckpoint conversation){
            conversation.sendMessage();
            quest.nextCheckPoint();
            return;
        }


        //TODO: UPDATE SCOREBOARD ETC.
    }
}
