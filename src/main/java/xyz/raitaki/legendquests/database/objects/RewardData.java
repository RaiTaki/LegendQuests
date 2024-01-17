package xyz.raitaki.legendquests.database.objects;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;

public class RewardData {

  private JSONObject data;
  private RewardTypeEnum type;
  private String value;

  public RewardData(JSONObject data) {
    this.data = data;
    type = RewardTypeEnum.valueOf((String) data.get("type"));
    value = (String) data.get("value");
  }

  public RewardTypeEnum getType() {
    return type;
  }

  public String getValue() {
    return value;
  }
}
