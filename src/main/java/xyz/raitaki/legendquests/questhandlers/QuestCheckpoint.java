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

  /**
   * @return JSON representation of the checkpoint
   */
  public abstract JSONObject getAsJSON();

  public enum CheckPointTypeEnum {
    INTERECT,
    CONVERSATION,
    KILL,
  }

  public enum TextTypeEnum {
    ACCEPT,
    DECLINE
  }

  /**
   * @return QuestBase of the checkpoint
   */
  public QuestBase getQuest() {
    return quest;
  }

  /**
   * @return the type of the checkpoint
   */
  public CheckPointTypeEnum getType() {
    return type;
  }

  /**
   * @return the value of the checkpoint
   */
  public String getValue() {
    return value;
  }
}
