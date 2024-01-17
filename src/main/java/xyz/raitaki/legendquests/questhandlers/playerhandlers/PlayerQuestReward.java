package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.ItemUtils;
import xyz.raitaki.legendquests.utils.TextUtils;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;

public class PlayerQuestReward {

  private PlayerQuest quest;
  private final RewardTypeEnum type;
  private final String value;

  public PlayerQuestReward(PlayerQuest playerQuest, RewardTypeEnum type, String value) {
    this.quest = playerQuest;
    this.type = type;
    this.value = value;
  }

  /**
   * give the reward to the player
   */
  public void giveReward() {
    Player player = quest.getQuestPlayer().getPlayer();
    LanguageConfig languageConfig = LanguageConfig.getInstance();
    switch (type) {
      case MONEY:
        EconomyUtils.giveMoney(player, Integer.parseInt(value));
        String text = languageConfig.getString("reward.money");
        text = TextUtils.replaceStrings(text, true, "{value}", value);
        TextUtils.sendCenteredMessage(player, text);
        break;
      case ITEM:
        ItemStack itemStack = ItemUtils.stringToItem(value);
        if (itemStack == null) {
          String error = languageConfig.getString("reward.item.error");
          String quest = languageConfig.getString("reward.item.quest");
          error = TextUtils.replaceStrings(error, true, "{value}", value);
          quest = TextUtils.replaceStrings(quest, true, "{questname}", this.quest.getQuestName());
          TextUtils.sendCenteredMessage(player, error);
          TextUtils.sendCenteredMessage(player, quest);
          return;
        }
        player.getInventory().addItem(itemStack);
        String item = languageConfig.getString("reward.item.give");
        item = TextUtils.replaceStrings(item, true, "{itemname}",
            itemStack.getItemMeta().getDisplayName());
        TextUtils.sendCenteredMessage(player, item);
        break;
      case XP:
        player.giveExp(Integer.parseInt(value));
        String xp = languageConfig.getString("reward.xp");
        xp = TextUtils.replaceStrings(xp, true, "{value}", value);
        TextUtils.sendCenteredMessage(player, xp);
        break;
    }

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
}
