package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.ItemUtils;
import xyz.raitaki.legendquests.utils.TextUtils;

public class PlayerQuestReward {

    private PlayerQuest quest;
    private final RewardTypeEnum type;
    private final String value;

    public PlayerQuestReward(PlayerQuest playerQuest, RewardTypeEnum type, String value) {
        this.quest = playerQuest;
        this.type = type;
        this.value = value;
    }

    public void giveReward(){
        Player player = quest.getQuestPlayer().getPlayer();
        switch (type){
            case MONEY:
                EconomyUtils.giveMoney(player, Integer.parseInt(value));
                TextUtils.sendCenteredMessage(player, "&a&l+" + value + "$");
                break;
            case ITEM:
                ItemStack itemStack = ItemUtils.stringToItem(value);
                if(itemStack == null) {
                    TextUtils.sendCenteredMessage(player, "&c&lERROR: &r&cType: ITEM: " + value);
                    TextUtils.sendCenteredMessage(player, "&c&lERROR: &r&cQuest name: " + quest.getQuestName());
                    return;
                }
                player.getInventory().addItem(itemStack);
                break;
            case XP:
                player.giveExp(Integer.parseInt(value));
                break;
        }

    }

    public JSONObject getAsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type.toString());
        jsonObject.put("value", value);
        return jsonObject;
    }
}
