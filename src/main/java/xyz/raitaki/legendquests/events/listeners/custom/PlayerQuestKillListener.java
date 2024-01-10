package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerQuestKillEvent;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerKillCheckpoint;

public class PlayerQuestKillListener implements Listener {

    @EventHandler
    public void onEntityKill(PlayerQuestKillEvent event) {
        PlayerQuest quest = event.getPlayerQuest();
        PlayerCheckpoint checkpoint = quest.getCheckPoint();
        if(quest.isCompleted()) return;
        if(!(checkpoint instanceof PlayerKillCheckpoint killCheckpoint)) return;

        Bukkit.broadcastMessage("PlayerQuestKillEvent called");

        killCheckpoint.incrementProgress();
        if(killCheckpoint.isComplete()) {
            quest.nextCheckPoint();
        }
    }
}
