package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;

public abstract class PlayerCheckpoint {

  private PlayerQuest quest;
  private CheckPointTypeEnum type;
  private String value;
  private boolean completed = false;

  public PlayerCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, String value,
      boolean completed) {
    this.quest = quest;
    this.type = type;
    this.value = value;
    this.completed = completed;
  }

  /**
   * @return JSON representation of the checkpoint
   */
  public abstract JSONObject getAsJSON();

  /**
   * @return PlayerQuest of the checkpoint
   */
  public PlayerQuest getQuest() {
    return quest;
  }

  /**
   * @return the type of the checkpoint
   */
  public CheckPointTypeEnum getType() {
    return type;
  }

  /**
   * @return the value of the checkpoint
   */
  public String getValue() {
    return value;
  }

  /**
   * @return true if the checkpoint is completed
   */
  public boolean isCompleted() {
    return completed;
  }

  /**
   * @param completed set the checkpoint to completed
   */
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }


}
