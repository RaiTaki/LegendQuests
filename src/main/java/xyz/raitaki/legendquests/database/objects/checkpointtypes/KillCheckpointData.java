package xyz.raitaki.legendquests.database.objects.checkpointtypes;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.CheckpointData;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;

public class KillCheckpointData extends CheckpointData {

  private int amount;
  private int counter;
  public KillCheckpointData(JSONObject data) {
    super(data);
    amount = (int) (long) data.get("amount");
    counter = (int) (long) data.get("counter");
  }

  public int getAmount() {
    return amount;
  }

  public int getCounter() {
    return counter;
  }
}
