package xyz.raitaki.legendquests.questhandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.ItemUtils;

public class QuestReward {

    private QuestBase questBase;
    private final RewardType type;
    private final String value;

    public QuestReward(QuestBase questBase, RewardType type, String value) {
        this.questBase = questBase;
        this.type = type;
        this.value = value;
    }

    public void giveReward(Player player){
        switch (type){
            case MONEY:
                EconomyUtils.giveMoney(player, Integer.parseInt(value));
                break;
            case ITEM:
                ItemStack itemStack = ItemUtils.deseriliseItemStack(value);
                if(itemStack == null) {
                    player.sendMessage("§c§lERROR: §r§cSomething went wrong while giving you your reward. Please contact an admin. With information down below.");
                    player.sendMessage("§c§lERROR: §r§cType: QUEST: " + questBase.getName());
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

    public RewardType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public enum RewardType {
        MONEY,
        ITEM,
        XP,
    }
}
