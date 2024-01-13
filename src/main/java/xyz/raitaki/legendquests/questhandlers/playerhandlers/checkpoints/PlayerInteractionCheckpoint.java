package xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;

public class PlayerInteractionCheckpoint extends PlayerCheckpoint {

    private String npcName;
    public PlayerInteractionCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, String text, boolean completed, String npcName) {
        super(quest, type, text, completed);
        this.npcName = npcName;
    }

    @Override
    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", getType().toString());
        jsonObject.put("value", getValue());
        jsonObject.put("npcName", getNpcName());
        jsonObject.put("completed", isCompleted());
        return jsonObject;
    }

    public String getNpcName() {
        return npcName;
    }
}
