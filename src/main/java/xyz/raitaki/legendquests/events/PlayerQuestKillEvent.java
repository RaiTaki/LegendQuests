package xyz.raitaki.legendquests.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerQuestKillEvent extends Event {

  private PlayerQuest playerQuest;
  private QuestPlayer questPlayer;
  private LivingEntity entity;
  private static final HandlerList HANDLERS_LIST = new HandlerList();

  public PlayerQuestKillEvent(@NotNull PlayerQuest playerQuest, @NotNull QuestPlayer questPlayer,
      @NotNull LivingEntity entity) {
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
   * @return the entity that was killed
   */
  public LivingEntity getEntity() {
    return entity;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }
}
