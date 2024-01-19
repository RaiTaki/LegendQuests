package xyz.raitaki.legendquests.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerNextCheckpointEvent extends Event implements Cancellable {

  private QuestPlayer questPlayer;
  private PlayerQuest playerQuest;
  private PlayerCheckpoint currentCheckpoint;
  private PlayerCheckpoint nextCheckpoint;
  private boolean cancelled;
  private static final HandlerList HANDLERS_LIST = new HandlerList();

  public PlayerNextCheckpointEvent(@NotNull QuestPlayer questPlayer,
      @NotNull PlayerQuest playerQuest, @NotNull PlayerCheckpoint currentCheckpoint,
      @NotNull PlayerCheckpoint nextCheckpoint) {
    this.questPlayer = questPlayer;
    this.playerQuest = playerQuest;
    this.currentCheckpoint = currentCheckpoint;
    this.nextCheckpoint = nextCheckpoint;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  /**
   * @return the quest player
   */
  public QuestPlayer getQuestPlayer() {
    return questPlayer;
  }

  /**
   * @return the quest that was updated
   */
  public PlayerQuest getPlayerQuest() {
    return playerQuest;
  }

  /**
   * @return the current checkpoint
   */
  public PlayerCheckpoint getCurrentCheckpoint() {
    return currentCheckpoint;
  }

  /**
   * @return the next checkpoint
   */
  public PlayerCheckpoint getNextCheckpoint() {
    return nextCheckpoint;
  }

  /**
   * @return whether the event is cancelled
   */
  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  /**
   * sets whether the event is cancelled
   *
   * @param cancel whether to cancel the event
   */
  @Override
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }
}
