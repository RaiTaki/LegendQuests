package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerQuestInteractEvent;

public class PlayerQuestInteractListener implements Listener {

  @EventHandler
  public void onPlayerQuestInteract(PlayerQuestInteractEvent event) {
    if (event.getPlayerQuest().isCompleted()) {
      return;
    }
    event.getPlayerQuest().nextCheckPoint();
  }
}
