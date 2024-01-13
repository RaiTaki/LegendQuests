package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;

public abstract class PlayerCheckpoint {

    private PlayerQuest quest;
    private CheckPointTypeEnum type;
    private String value;
    private boolean completed = false;

    public PlayerCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, String value, boolean completed) {
        this.quest = quest;
        this.type = type;
        this.value = value;
        this.completed = completed;
    }

    public void addProgress(){
        quest.nextCheckPoint();
    }

    public abstract JSONObject getAsJSON();

    public PlayerQuest getQuest() {
        return quest;
    }

    public CheckPointTypeEnum getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }


}
