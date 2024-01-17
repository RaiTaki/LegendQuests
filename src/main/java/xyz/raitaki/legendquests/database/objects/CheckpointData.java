package xyz.raitaki.legendquests.database.objects;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;

public abstract class CheckpointData {
  private JSONObject data;
  private CheckPointTypeEnum type;
  private String value;
  private boolean completed;

  public CheckpointData(JSONObject data) {
    this.data = data;
    type = CheckPointTypeEnum.valueOf((String) data.get("type"));
    value = (String) data.get("value");
    completed = (boolean) data.get("completed");
  }

  public CheckPointTypeEnum getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  public boolean isCompleted() {
    return completed;
  }
  public JSONObject getData() {
    return data;
  }

}
