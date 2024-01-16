package xyz.raitaki.legendquests.questhandlers;

import java.util.LinkedList;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.gui.QuestGui;

public class QuestBase {

  private final String name;
  private final String description;
  private final LinkedList<QuestReward> rewards;
  private final LinkedList<QuestCheckpoint> checkPoints;
  private QuestGui questGUI;
  private long time;

  public QuestBase(String name, String description, long time) {
    this.name = name;
    this.description = description;
    rewards = new LinkedList<>();
    checkPoints = new LinkedList<>();
    this.time = time;

    QuestManager.getBaseQuests().add(this);
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
  public void addCheckPoint(QuestCheckpoint checkPoint) {
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
  public String getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("name", name);
    jsonObject.put("description", description);

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
    return jsonObject.toJSONString();
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
}
