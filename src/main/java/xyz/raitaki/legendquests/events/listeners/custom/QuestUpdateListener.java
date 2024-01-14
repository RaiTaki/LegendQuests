package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class QuestUpdateListener implements Listener {

    @EventHandler
    public void onQuestUpdate(QuestUpdateEvent event) {
        new BukkitRunnable(){
            @Override
            public void run() {
                QuestManager.getQuestPlayers().values().forEach(questplayer->{
                    Bukkit.broadcastMessage(questplayer.getQuests().toString());
                    QuestManager.updatePlayerQuestBaseQuest(questplayer, event.getQuestBase());
                });
            }
        }.runTaskAsynchronously(LegendQuests.getInstance());
    }
}