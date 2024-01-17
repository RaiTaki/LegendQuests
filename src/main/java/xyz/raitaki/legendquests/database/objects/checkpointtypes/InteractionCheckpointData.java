package xyz.raitaki.legendquests.database.objects.checkpointtypes;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.CheckpointData;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;

public class InteractionCheckpointData extends CheckpointData {

  private String npcName;
  public InteractionCheckpointData(JSONObject data) {
    super(data);
    npcName = (String) data.get("npcName");
  }

  public String getNpcName() {
    return npcName;
  }
}
