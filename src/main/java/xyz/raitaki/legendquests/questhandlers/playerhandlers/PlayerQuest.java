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

  /**
   * @param checkpoint add the checkpoint to the quest
   */
  public void addCheckpoint(PlayerCheckpoint checkpoint) {
    checkpoints.add(checkpoint);
  }

  /**
   * @param checkPoint set the checkpoint of the quest
   */
  public void setCheckPoint(PlayerCheckpoint checkPoint) {
    this.checkPoint = checkPoint;
  }

  /**
   * move the player to the next checkpoint
   */
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

  /**
   * move the player to the previous checkpoint
   */
  public void previousCheckPoint() {
    int index = checkpoints.indexOf(checkPoint);
    checkPoint.setCompleted(false);
    if (index != 0) {
      checkPoint = checkpoints.get(index - 1);
      checkPoint.setCompleted(false);
    }
  }

  /**
   * @param reward add the reward to the quest
   */
  public void addReward(PlayerQuestReward reward) {
    rewards.add(reward);
  }

  /**
   * @return the quest base of the quest
   */
  public QuestBase getQuest() {
    return questBase;
  }

  /**
   * @return the player of the quest
   */
  public QuestPlayer getQuestPlayer() {
    return player;
  }

  /**
   * @return the checkpoint of the quest
   */
  public PlayerCheckpoint getCheckPoint() {
    return checkPoint;
  }

  /**
   * @return true if the quest is completed
   */
  public boolean isCompleted() {
    return completed;
  }

  /**
   * @return the name of the quest
   */
  public String getQuestName() {
    return questName;
  }

  /**
   * @return the checkpoints of the quest
   */
  public LinkedList<PlayerCheckpoint> getCheckpoints() {
    return checkpoints;
  }

  /**
   * give the reward to the player
   */
  public void giveReward() {
    for (PlayerQuestReward reward : rewards) {
      reward.giveReward();
    }
  }

  /**
   * update the checkpoint of the quest
   */
  public void updateCheckpoint() {
    for (PlayerCheckpoint checkpoint : checkpoints) {
      if (checkpoint.isCompleted()) {
        continue;
      }
      this.checkPoint = checkpoint;
      return;
    }
  }

  /**
   * @return JSON representation of the quest
   */
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

  /**
   * @param completed set if the quest is completed
   */
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * @return the description of the quest
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the formatted remaining time of the quest
   */
  public String getRemainingTimeFormatted() {
    return TextUtils.formatDateTime(getCalculatedRemainingTime());
  }

  /**
   * @return the remaining time of the quest in milliseconds
   */
  public long getCalculatedRemainingTime() {
    long time = remainingTime - (System.currentTimeMillis() - startTime);
    if (time < 0) {
      resetQuest();
      return 0;
    }
    return time;
  }

  /**
   * reset the quest
   */
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
