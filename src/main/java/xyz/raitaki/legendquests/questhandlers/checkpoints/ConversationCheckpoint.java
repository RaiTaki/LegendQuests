package xyz.raitaki.legendquests.questhandlers.checkpoints;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.utils.TextUtils;

public class ConversationCheckpoint extends QuestCheckpoint {

  private String npcName;
  private String acceptText;
  private String declineText;

  public ConversationCheckpoint(QuestBase quest, CheckPointTypeEnum type, String text,
      String npcName, String acceptText, String declineText) {
    super(quest, type, text);
    this.npcName = npcName;
    this.acceptText = acceptText;
    this.declineText = declineText;
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
    jsonObject.put("acceptText", getAcceptText());
    jsonObject.put("declineText", getDeclineText());
    return jsonObject;
  }

  /**
   * @return the accept text
   */
  public String getAcceptText() {
    return acceptText;
  }

  /**
   * @return the decline text
   */
  public String getDeclineText() {
    return declineText;
  }
}
