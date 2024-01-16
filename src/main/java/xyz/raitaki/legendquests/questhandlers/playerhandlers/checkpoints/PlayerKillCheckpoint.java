package xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;

public class PlayerKillCheckpoint extends PlayerCheckpoint {

  int counter = 0;
  int amount = 0;

  public PlayerKillCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, String targetName,
      boolean completed, int amount, int counter) {
    super(quest, type, targetName, completed);
    this.amount = amount;
    this.counter = counter;
  }

  /**
   * increment the progress of the checkpoint
   */
  public void incrementProgress() {
    counter++;
  }

  /**
   * @return the counter of the checkpoint
   */
  public int getCounter() {
    return counter;
  }

  /**
   * @return the amount of the checkpoint
   */
  public int getAmount() {
    return amount;
  }

  /**
   * @return true if the checkpoint is completed
   */
  public boolean isComplete() {
    return counter >= amount;
  }

  /**
   * @return JSON representation of the checkpoint
   */
  @Override
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", getType().toString());
    jsonObject.put("value", getValue());
    jsonObject.put("amount", amount);
    jsonObject.put("counter", counter);
    jsonObject.put("completed", isCompleted());
    return jsonObject;
  }
}
