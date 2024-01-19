package xyz.raitaki.legendquests.database.objects.checkpointtypes;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.CheckpointData;

public class KillCheckpointData extends CheckpointData {

  private int amount;
  private int counter;

  public KillCheckpointData(JSONObject data, boolean player) {
    super(data, player);
    amount = (int) (long) data.get("amount");

    if (player) {
      counter = (int) (long) data.get("counter");
    }
  }

  public int getAmount() {
    return amount;
  }

  public int getCounter() {
    return counter;
  }
}
