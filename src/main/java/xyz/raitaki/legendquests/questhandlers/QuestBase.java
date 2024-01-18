package xyz.raitaki.legendquests.questhandlers;

import java.util.LinkedList;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.DatabaseConnection;
import xyz.raitaki.legendquests.questhandlers.gui.QuestGui;

public class QuestBase {

  private String name;
  private String description;
  private final LinkedList<QuestReward> rewards;
  private final LinkedList<QuestCheckpoint> checkPoints;
  private QuestGui questGUI;
  private long time;
  private String nextQuestName;
  private String questId;

  public QuestBase(String questId, String name, String description, long time, String nextQuestName) {
    this.name = name;
    this.questId = questId;
    this.description = description;
    rewards = new LinkedList<>();
    checkPoints = new LinkedList<>();
    this.time = time;
    this.nextQuestName = nextQuestName;

    QuestManager.getBaseQuests().add(this);
  }

  public QuestBase(String name) {
    this.name = name;
    this.description = "No description";
    this.time = 0;
    rewards = new LinkedList<>();
    checkPoints = new LinkedList<>();
    questId = QuestManager.getNextQuestId();

    QuestManager.getBaseQuests().add(this);

    buildGUI();

    DatabaseConnection.insertQuest(this.getAsJSON());
  }

  /**
   * add the reward to the quest
   * @param reward the reward to add
   */
  public void addReward(QuestReward reward) {
    rewards.add(reward);
  }

  /**
   * add the reward to the quest
   * @param checkPoint the checkpoint to add
   */
  public void addCheckpoint(QuestCheckpoint checkPoint) {
    checkPoints.add(checkPoint);
  }

  /**
   * @return the name of the quest
   */
  public String getName() {
    return name;
  }

  /**
   * @return the description of the quest
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the rewards of the quest
   */
  public LinkedList<QuestReward> getRewards() {
    return rewards;
  }

  /**
   * @return the checkpoints of the quest
   */
  public LinkedList<QuestCheckpoint> getCheckPoints() {
    return checkPoints;
  }

  /**
   * @return JSON representation of the quest
   */
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", questId);
    jsonObject.put("name", name);
    jsonObject.put("description", description);
    jsonObject.put("time", time);
    jsonObject.put("nextQuestName", nextQuestName);

    JSONArray rewardsArray = new JSONArray();
    for (QuestReward reward : rewards) {
      rewardsArray.add(reward.getAsJSON());
    }

    JSONArray checkPointsArray = new JSONArray();
    for (QuestCheckpoint checkPoint : checkPoints) {
      checkPointsArray.add(checkPoint.getAsJSON());
    }

    jsonObject.put("rewards", rewardsArray);
    jsonObject.put("checkPoints", checkPointsArray);
    return jsonObject;
  }

  /**
   * function to build the GUI of the quest
   */
  public void buildGUI() {
    questGUI = new QuestGui(this);
  }

  /**
   * @return QuestGui of the quest
   */
  public QuestGui getQuestGUI() {
    return questGUI;
  }

  /**
   * show the GUI to the player
   * @param player the player to show the GUI
   */
  public void showGui(Player player) {
    questGUI.openMainGUI(player);
  }

  /**
   * @return the time of the quest
   */
  public long getTime() {
    return time;
  }

  /**
   * @return the next quest name
   */
  public String getNextQuestName() {
    return nextQuestName;
  }

  /**
   * @param time the time to set
   */
  public void setTime(long time) {
    this.time = time;
  }

  /**
   * @param questName the quest name to set
   */
  public void setQuestName(String questName) {
    this.name = questName;
  }

  /**
   * @param questDescription the quest description to set
   */
  public void setQuestDescription(String questDescription) {
    this.description = questDescription;
  }

  /**
   * @param nextQuestName the next quest name
   */
  public void setNextQuestName(String nextQuestName) {
    this.nextQuestName = nextQuestName;
  }

  /**
   * @return the quest id
   */
  public String getQuestId() {
    return questId;
  }
}
