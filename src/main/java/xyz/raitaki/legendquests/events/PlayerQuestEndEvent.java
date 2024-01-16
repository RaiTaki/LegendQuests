package xyz.raitaki.legendquests.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class PlayerQuestEndEvent extends Event {

  private QuestPlayer questPlayer;
  private PlayerQuest playerQuest;
  private static final HandlerList HANDLERS_LIST = new HandlerList();

  public PlayerQuestEndEvent(@NotNull QuestPlayer questPlayer, @NotNull PlayerQuest playerQuest) {
    this.questPlayer = questPlayer;
    this.playerQuest = playerQuest;
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

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLERS_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS_LIST;
  }
}
