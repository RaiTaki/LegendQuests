package xyz.raitaki.legendquests.events.listeners.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerConversationCheckpoint;

public class PlayerCheckpointListener implements Listener {

  @EventHandler
  public void onCheckpointUpdate(PlayerNextCheckpointEvent event) {
    if (event.getPlayerQuest().isCompleted()) {
      return;
    }
    PlayerCheckpoint nextCheckpoint = event.getNextCheckpoint();

    if (nextCheckpoint instanceof PlayerConversationCheckpoint conversation) {
      conversation.sendMessage();
    }
  }
}
