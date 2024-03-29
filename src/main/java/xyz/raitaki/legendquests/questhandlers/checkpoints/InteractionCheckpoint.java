package xyz.raitaki.legendquests.questhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;

public class InteractionCheckpoint extends QuestCheckpoint {

  private String npcName;

  public InteractionCheckpoint(QuestBase quest, CheckPointTypeEnum type, String text,
      String npcName) {
    super(quest, type, text);
    this.npcName = npcName;
  }

  /**
   * @return the name of the npc
   */
  public String getNpcName() {
    return npcName;
  }

  /**
   * @return JSON representation of the checkpoint
   */
  @Override
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", getType().toString());
    jsonObject.put("value", getValue());
    jsonObject.put("npcName", getNpcName());

    return jsonObject;
  }
}
