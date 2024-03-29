package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerAnswerEvent;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;

public class PlayerAnswerListener implements Listener {

  @EventHandler
  public void onPlayerAnswer(PlayerAnswerEvent event) {
    PlayerQuest playerQuest = event.getPlayerQuest();

    if (playerQuest.isCompleted()) {
      return;
    }

    playerQuest.nextCheckPoint();
  }
}
