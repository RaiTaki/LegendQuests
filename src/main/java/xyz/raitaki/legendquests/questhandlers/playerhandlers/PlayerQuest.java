package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import static xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum.CONVERSATION;

import java.util.LinkedList;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.events.PlayerQuestEndEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.utils.TextUtils;

public class PlayerQuest {

  private QuestBase questBase;
  private QuestPlayer player;
  private PlayerCheckpoint checkpoint;
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
   * @param checkpoint set the checkpoint of the quest
   */
  public void setCheckpoint(PlayerCheckpoint checkpoint) {
    this.checkpoint = checkpoint;
  }

  /**
   * move the player to the next checkpoint
   */
  public void nextCheckPoint() {
    int index = checkpoints.indexOf(checkpoint);
    if (index == checkpoints.size() - 1) {
      PlayerQuestEndEvent event = new PlayerQuestEndEvent(player, this);
      Bukkit.getPluginManager().callEvent(event);
      completed = true;
      String nextQuestName = questBase.getNextQuestName();

      if (nextQuestName != null) {
        QuestBase nextQuest = QuestManager.getQuestBaseByName(nextQuestName);
        if(nextQuest == null) {
          return;
        }
        QuestManager.addBaseQuestToPlayer(player.getPlayer(), nextQuest);
      }
    } else {
      checkpoint.setCompleted(true);
      checkpoint = checkpoints.get(index + 1);
      PlayerNextCheckpointEvent event = new PlayerNextCheckpointEvent(player, this, checkpoint,
          checkpoints.get(index + 1));
      Bukkit.getPluginManager().callEvent(event);
    }
  }

  /**
   * move the player to the previous checkpoint
   */
  public void previousCheckPoint() {
    int index = checkpoints.indexOf(checkpoint);
    checkpoint.setCompleted(false);
    if (index != 0) {
      checkpoint = checkpoints.get(index - 1);
      checkpoint.setCompleted(false);
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
  public PlayerCheckpoint getCheckpoint() {
    return checkpoint;
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
  public void updateCheckpoint(boolean load) {
    for (PlayerCheckpoint checkpoint : checkpoints) {
      if (checkpoint.isCompleted()) {
        continue;
      }
      this.checkpoint = checkpoint;
      if(this.checkpoint.getType() == CONVERSATION && !isCompleted() && load) {
        this.checkpoint = checkpoints.get(checkpoints.indexOf(checkpoint) - 1);
        this.checkpoint.setCompleted(false);
        this.completed = false;
      }
      return;
    }

    if(this.checkpoint == null || this.checkpoint.isCompleted() && !this.completed) {
      nextCheckPoint();
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
    jsonObject.put("id", questBase.getQuestId());

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
    this.checkpoint = checkpoints.get(0);
    for (PlayerCheckpoint checkpoint : checkpoints) {
      checkpoint.setCompleted(false);
    }
  }

  /**
   * @param remainingTime set the remaining time of the quest
   */
  public void setRemainingTime(long remainingTime) {
    this.remainingTime = remainingTime;
  }

  /**
   * @param questName the quest name to set
   */
  public void setQuestName(String questName) {
    this.questName = questName;
  }

  /**
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
