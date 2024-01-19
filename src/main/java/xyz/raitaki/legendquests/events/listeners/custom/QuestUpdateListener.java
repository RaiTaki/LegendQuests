package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.database.DatabaseConnection;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestManager;

public class QuestUpdateListener implements Listener {

  @EventHandler
  public void onQuestUpdate(QuestUpdateEvent event) {
    new BukkitRunnable() {
      @Override
      public void run() {
        QuestManager.getQuestPlayers().values().forEach(questplayer -> {
          QuestManager.updatePlayerQuestBaseQuest(questplayer, event.getQuestBase());
        });
        DatabaseConnection.updateQuest(event.getQuestBase().getAsJSON());
      }
    }.runTaskAsynchronously(LegendQuests.getInstance());
  }
}
