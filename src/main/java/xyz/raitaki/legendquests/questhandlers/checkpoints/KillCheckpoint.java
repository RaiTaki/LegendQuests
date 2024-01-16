package xyz.raitaki.legendquests.questhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;

public class KillCheckpoint extends QuestCheckpoint {

  private int amount;

  public KillCheckpoint(QuestBase quest, CheckPointTypeEnum type, String targetName, int amount) {
    super(quest, type, targetName);
    this.amount = amount;
  }

  /**
   * @return the amount of the target to kill
   */
  public int getAmount() {
    return amount;
  }

  /**
   * @return JSON representation of the checkpoint
   */
  @Override
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", getType().toString());
    jsonObject.put("value", getValue());
    jsonObject.put("amount", getAmount());
    return jsonObject;
  }
}
