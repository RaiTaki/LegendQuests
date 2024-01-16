package xyz.raitaki.legendquests.questhandlers;

import static xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum.ITEM;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.ItemUtils;
import xyz.raitaki.legendquests.utils.TextUtils;

public class QuestReward {

  private QuestBase questBase;
  private final RewardTypeEnum type;
  private final String value;

  public QuestReward(QuestBase questBase, RewardTypeEnum type, String value) {
    this.questBase = questBase;
    this.type = type;
    this.value = value;
  }

  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", type.toString());
    jsonObject.put("value", value);
    return jsonObject;
  }

  public RewardTypeEnum getType() {
    return type;
  }

  public String getValue() {
    if (type == ITEM) {
      return "To see it edit the reward";
    }
    return value;
  }

  public enum RewardTypeEnum {
    MONEY,
    ITEM,
    XP,
  }
}
