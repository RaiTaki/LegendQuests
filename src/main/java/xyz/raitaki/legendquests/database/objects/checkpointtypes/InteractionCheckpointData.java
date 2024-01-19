package xyz.raitaki.legendquests.database.objects.checkpointtypes;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.CheckpointData;

public class InteractionCheckpointData extends CheckpointData {

  private String npcName;

  public InteractionCheckpointData(JSONObject data, boolean player) {
    super(data, player);
    npcName = (String) data.get("npcName");
  }

  public String getNpcName() {
    return npcName;
  }
}
