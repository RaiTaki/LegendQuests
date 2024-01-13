package xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;

public class PlayerConversationCheckpoint extends PlayerCheckpoint {

    private String npcName;
    public PlayerConversationCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, boolean completed, String text, String npcName) {
        super(quest, type, text, completed);
        this.npcName = npcName;
    }

    public void sendMessage(){
        getQuest().getQuestPlayer().getPlayer().sendMessage(getValue());
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
        jsonObject.put("completed", isCompleted());
        return jsonObject;
    }
}
