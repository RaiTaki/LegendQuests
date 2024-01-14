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

  public void sendMessage(Player player) {
    TextUtils.sendCenteredMessage(player,
        "&m                                                                    ");
    TextUtils.sendCenteredMessage(player, "&a&l" + npcName);
    TextUtils.sendCenteredMessage(player, "<SOLID:7d7d7d> " + getValue());
    TextUtils.sendCenteredMessage(player, "&a&l " + acceptText + "      &c&l" + declineText);
    TextUtils.sendCenteredMessage(player,
        "&m                                                                    ");
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

  public String getAcceptText() {
    return acceptText;
  }

  public String getDeclineText() {
    return declineText;
  }
}
