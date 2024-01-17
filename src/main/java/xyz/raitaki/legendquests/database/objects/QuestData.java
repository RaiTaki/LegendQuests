package xyz.raitaki.legendquests.database.objects;

import java.util.LinkedList;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.ConversationCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.InteractionCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.KillCheckpointData;

public class QuestData {

  private JSONObject data;
  private String name;
  private String description;
  private List<CheckpointData> checkpoints;
  private List<RewardData> rewards;
  private boolean completed;
  private long time;

  public QuestData(JSONObject data) {
    this.data = data;
    checkpoints = loadCheckpoints();
    rewards = loadRewards();
    name = (String) data.get("name");
    description = (String) data.get("description");
    completed = (boolean) data.get("completed");
    time = (long) data.get("remainingTime");
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public boolean isCompleted() {
    return completed;
  }

  public JSONObject getAsJSON() {
    return data;
  }

  public Long getTime() {
    return time;
  }

  public List<CheckpointData> getCheckpoints() {
    return checkpoints;
  }

  public List<RewardData> getRewards() {
    return rewards;
  }

  private List<RewardData> loadRewards() {
    LinkedList<RewardData> rewards = new LinkedList<>();
    for (Object reward : (JSONArray) data.get("rewards")) {
      rewards.add(new RewardData((JSONObject) reward));
    }
    return rewards;
  }

  private List<CheckpointData> loadCheckpoints() {
    LinkedList<CheckpointData> checkpoints = new LinkedList<>();
    for (Object checkpoint : (JSONArray) data.get("checkPoints")) {
      String type = (String) ((JSONObject) checkpoint).get("type");

      if (type.equals("KILL")) {
        checkpoints.add(new KillCheckpointData((JSONObject) checkpoint));
      } else if (type.equals("INTERACT")) {
        checkpoints.add(new InteractionCheckpointData((JSONObject) checkpoint));
      } else {
        checkpoints.add(new ConversationCheckpointData((JSONObject) checkpoint));
      }
    }
    return checkpoints;
  }
}
