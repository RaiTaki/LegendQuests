package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointType;

public abstract class PlayerCheckpoint {

    private PlayerQuest quest;
    private CheckPointType type;
    private String value;
    private boolean completed = false;

    public PlayerCheckpoint(PlayerQuest quest, CheckPointType type, String value, boolean completed) {
        this.quest = quest;
        this.type = type;
        this.value = value;
        this.completed = completed;
    }

    public abstract JSONObject getAsJSON();

    public PlayerQuest getQuest() {
        return quest;
    }

    public CheckPointType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isCompleted() {
        return completed;
    }
}
