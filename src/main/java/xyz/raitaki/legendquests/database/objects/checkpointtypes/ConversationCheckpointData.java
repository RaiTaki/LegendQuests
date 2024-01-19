package xyz.raitaki.legendquests.database.objects.checkpointtypes;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.CheckpointData;

public class ConversationCheckpointData extends CheckpointData {

  private String npcName;
  private String acceptText;
  private String declineText;

  public ConversationCheckpointData(JSONObject data, boolean player) {
    super(data, player);
    npcName = (String) data.get("npcName");
    acceptText = (String) data.get("acceptText");
    declineText = (String) data.get("declineText");
  }

  public String getNpcName() {
    return npcName;
  }

  public String getAcceptText() {
    return acceptText;
  }

  public String getDeclineText() {
    return declineText;
  }
}
