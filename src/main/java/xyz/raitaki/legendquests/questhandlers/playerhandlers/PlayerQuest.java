package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.events.PlayerQuestEndEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.utils.TextUtils;

public class PlayerQuest {

  private QuestBase questBase;
  private QuestPlayer player;
  private PlayerCheckpoint checkPoint;
  private LinkedList<PlayerCheckpoint> checkpoints;
  private LinkedList<PlayerQuestReward> rewards;
  private boolean completed;
  private String questName;
  private String description;
  private long remainingTime;
  private long startTime;

  public PlayerQuest(QuestBase quest, QuestPlayer player, long remainingTime) {
    this.questName = quest.getName();
    this.description = quest.getDescription();
    this.questBase = quest;
    this.player = player;
    this.checkpoints = new LinkedList<>();
    this.rewards = new LinkedList<>();
    this.remainingTime = remainingTime;
    this.startTime = System.currentTimeMillis();
  }

  public void addCheckpoint(PlayerCheckpoint checkpoint) {
    checkpoints.add(checkpoint);
  }

  public void setCheckPoint(PlayerCheckpoint checkPoint) {
    this.checkPoint = checkPoint;
  }

  public void nextCheckPoint() {
    int index = checkpoints.indexOf(checkPoint);
    if (index == checkpoints.size() - 1) {
      PlayerQuestEndEvent event = new PlayerQuestEndEvent(player, this);
      Bukkit.getPluginManager().callEvent(event);
      completed = true;
    } else {
      checkPoint.setCompleted(true);
      checkPoint = checkpoints.get(index + 1);
      PlayerNextCheckpointEvent event = new PlayerNextCheckpointEvent(player, this, checkPoint,
          checkpoints.get(index + 1));
      Bukkit.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
        checkPoint = checkpoints.get(index);
        checkPoint.setCompleted(false);
      }
    }
  }

  public void previousCheckPoint() {
    int index = checkpoints.indexOf(checkPoint);
    checkPoint.setCompleted(false);
    if (index != 0) {
      checkPoint = checkpoints.get(index - 1);
      checkPoint.setCompleted(false);
    }
  }

  public void addReward(PlayerQuestReward reward) {
    rewards.add(reward);
  }

  public QuestBase getQuest() {
    return questBase;
  }

  public QuestPlayer getQuestPlayer() {
    return player;
  }

  public PlayerCheckpoint getCheckPoint() {
    return checkPoint;
  }

  public boolean isCompleted() {
    return completed;
  }

  public String getQuestName() {
    return questName;
  }

  public LinkedList<PlayerCheckpoint> getCheckpoints() {
    return checkpoints;
  }

  public void giveReward() {
    for (PlayerQuestReward reward : rewards) {
      reward.giveReward();
    }
  }

  public void updateCheckpoint() {
    for (PlayerCheckpoint checkpoint : checkpoints) {
      if (checkpoint.isCompleted()) {
        continue;
      }
      this.checkPoint = checkpoint;
      return;
    }
  }

  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", questBase.getName());
    jsonObject.put("description", questBase.getDescription());
    jsonObject.put("completed", completed);
    jsonObject.put("remainingTime", getCalculatedRemainingTime());

    JSONArray rewardsArray = new JSONArray();
    for (PlayerQuestReward reward : rewards) {
      rewardsArray.add(reward.getAsJSON());
    }

    JSONArray checkPointsArray = new JSONArray();
    for (PlayerCheckpoint checkPoint : checkpoints) {
      checkPointsArray.add(checkPoint.getAsJSON());
    }

    jsonObject.put("rewards", rewardsArray);
    jsonObject.put("checkPoints", checkPointsArray);
    return jsonObject;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public String getDescription() {
    return description;
  }

  public String getRemainingTimeFormatted() {
    return TextUtils.formatDateTime(getCalculatedRemainingTime());
  }

  public long getCalculatedRemainingTime() {
    long time = remainingTime - (System.currentTimeMillis() - startTime);
    if (time < 0) {
      resetQuest();
      return 0;
    }
    return time;
  }

  public void resetQuest() {
    this.startTime = System.currentTimeMillis();
    this.remainingTime = questBase.getTime();
    this.completed = false;
    this.checkPoint = checkpoints.get(0);
    for (PlayerCheckpoint checkpoint : checkpoints) {
      checkpoint.setCompleted(false);
    }
  }
}
