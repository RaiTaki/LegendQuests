package xyz.raitaki.legendquests.questhandlers;

import org.json.simple.JSONObject;

public abstract class QuestCheckpoint {

    private QuestBase quest;
    private CheckPointTypeEnum type;
    private String value;

    public QuestCheckpoint(QuestBase quest, CheckPointTypeEnum type, String value) {
        this.quest = quest;
        this.type = type;
        this.value = value;
    }

    public abstract JSONObject getAsJSON();

    public enum CheckPointTypeEnum {
        INTERECT,
        CONVERSATION,
        KILL,
    }

    public enum TextTypeEnum{
        ACCEPT,
        DECLINE
    }

    public QuestBase getQuest() {
        return quest;
    }

    public CheckPointTypeEnum getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
