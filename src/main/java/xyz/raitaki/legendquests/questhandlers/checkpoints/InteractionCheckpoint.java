package xyz.raitaki.legendquests.questhandlers.checkpoints;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;

public class InteractionCheckpoint extends QuestCheckpoint {

    private String npcName;

    public InteractionCheckpoint(QuestBase quest, CheckPointType type, String text, String npcName) {
        super(quest, type, text);
        this.npcName = npcName;
    }

    public void sendMessage(Player player){
        player.sendMessage(getValue());
    }

    public String getNpcName() {
        return npcName;
    }

    @Override
    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", getType().toString());
        jsonObject.put("value", getValue());
        jsonObject.put("npcName", getNpcName());
        return jsonObject;
    }
}
