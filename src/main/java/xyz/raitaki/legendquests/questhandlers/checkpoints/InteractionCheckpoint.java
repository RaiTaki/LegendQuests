package xyz.raitaki.legendquests.questhandlers.checkpoints;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.utils.TextUtils;

public class InteractionCheckpoint extends QuestCheckpoint {

    private String npcName;

    public InteractionCheckpoint(QuestBase quest, CheckPointTypeEnum type, String text, String npcName) {
        super(quest, type, text);
        this.npcName = npcName;
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
