package xyz.raitaki.legendquests.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerQuestInteractEvent extends Event implements Cancellable {

  private static final HandlerList HANDLERS_LIST = new HandlerList();
  private boolean cancelled;

  private PlayerQuest playerQuest;
  private QuestPlayer questPlayer;
  private Entity entity;

  public PlayerQuestInteractEvent(@NotNull PlayerQuest playerQuest,
      @NotNull QuestPlayer questPlayer, @NotNull Entity entity) {
    this.playerQuest = playerQuest;
    this.questPlayer = questPlayer;
    this.entity = entity;
  }

  /**
   * @return the quest that was updated
   */
  public PlayerQuest getPlayerQuest() {
    return playerQuest;
  }

  /**
   * @return the quest player
   */
  public QuestPlayer getQuestPlayer() {
    return questPlayer;
  }

  /**
   * @return the entity that was interacted with
   */
  public Entity getEntity() {
    return entity;
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
   * @param cancel whether to cancel the event
   */
  @Override
  public void setCancelled(boolean cancel) {
    cancelled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }
}
