package xyz.raitaki.legendquests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerCheckpointListener implements Listener {

    @EventHandler
    public void onCheckpointUpdate(PlayerNextCheckpointEvent event){
        PlayerQuest quest = event.getPlayerQuest();
        QuestPlayer player = event.getQuestPlayer();
        PlayerCheckpoint currentCheckpoint = event.getCurrentCheckpoint();
        PlayerCheckpoint nextCheckpoint = event.getNextCheckpoint();

        if(nextCheckpoint == null) return;

        //TODO: UPDATE SCOREBOARD ETC.
    }
}
