package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.ItemUtils;

public class PlayerQuestReward {

    private PlayerQuest quest;
    private final RewardType type;
    private final String value;

    public PlayerQuestReward(PlayerQuest playerQuest, RewardType type, String value) {
        this.quest = playerQuest;
        this.type = type;
        this.value = value;
    }

    public void giveReward(){
        Player player = quest.getQuestPlayer().getPlayer();
        switch (type){
            case MONEY:
                EconomyUtils.giveMoney(player, Integer.parseInt(value));
                player.sendMessage("§a§lQUEST: §r§aYou have been given §e" + value + "§a coins!");
                break;
            case ITEM:
                ItemStack itemStack = ItemUtils.deseriliseItemStack(value);
                if(itemStack == null) {
                    player.sendMessage("§c§lERROR: §r§cSomething went wrong while giving you your reward. Please contact an admin. With information down below.");
                    player.sendMessage("§c§lERROR: §r§cType: QUEST: " + quest.getQuestName());
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

    public enum RewardType {
        MONEY,
        ITEM,
        XP,
    }
}
