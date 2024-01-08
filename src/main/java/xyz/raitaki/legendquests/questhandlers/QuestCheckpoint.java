package xyz.raitaki.legendquests.questhandlers;

import org.json.simple.JSONObject;

public abstract class QuestCheckpoint {

    private QuestBase quest;
    private CheckPointType type;
    private String value;

    public QuestCheckpoint(QuestBase quest, CheckPointType type, String value) {
        this.quest = quest;
        this.type = type;
        this.value = value;
    }

    public abstract JSONObject getAsJSON();

    public enum CheckPointType{
        INTERECT,
        CONVERSATION,
        KILL,
    }

    public QuestBase getQuest() {
        return quest;
    }

    public CheckPointType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
