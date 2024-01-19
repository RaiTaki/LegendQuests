package xyz.raitaki.legendquests.questhandlers;

import static xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum.ITEM;

import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.utils.ItemUtils;

public class QuestReward {

  private QuestBase questBase;
  private final RewardTypeEnum type;
  private final String value;

  public QuestReward(QuestBase questBase, RewardTypeEnum type, String value) {
    this.questBase = questBase;
    this.type = type;
    this.value = value;
  }

  /**
   * @return JSON representation of the reward
   */
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", type.toString());
    jsonObject.put("value", value);
    return jsonObject;
  }

  /**
   * @return the type of the reward
   */
  public RewardTypeEnum getType() {
    return type;
  }

  /**
   * @return the value of the reward
   */
  public String getValue() {
    if (type == ITEM) {
      ItemStack item = ItemUtils.stringToItem(value);
      if (item == null) {
        return "To see it edit the reward";
      }
      return value;
    }
    return value;
  }

  public enum RewardTypeEnum {
    MONEY,
    ITEM,
    XP,
  }
}
