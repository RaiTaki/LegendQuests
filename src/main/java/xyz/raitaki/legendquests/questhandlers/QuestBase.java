package xyz.raitaki.legendquests.questhandlers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.LinkedList;

public class QuestBase {

    private final String name;
    private final String description;
    private final LinkedList<QuestReward> rewards;
    private final LinkedList<QuestCheckpoint> checkPoints;

    public QuestBase(String name, String description) {
        this.name = name;
        this.description = description;
        rewards = new LinkedList<>();
        checkPoints = new LinkedList<>();

        QuestManager.getBaseQuests().add(this);
    }

    public void addReward(QuestReward reward) {
        rewards.add(reward);
    }

    public void addCheckPoint(QuestCheckpoint checkPoint) {
        checkPoints.add(checkPoint);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LinkedList<QuestReward> getRewards() {
        return rewards;
    }

    public LinkedList<QuestCheckpoint> getCheckPoints() {
        return checkPoints;
    }

    public String getAsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("description", description);

        JSONArray rewardsArray = new JSONArray();
        for(QuestReward reward : rewards) {
            rewardsArray.add(reward.getAsJSON());
        }

        JSONArray checkPointsArray = new JSONArray();
        for(QuestCheckpoint checkPoint : checkPoints) {
            checkPointsArray.add(checkPoint.getAsJSON());
        }

        jsonObject.put("rewards", rewardsArray);
        jsonObject.put("checkPoints", checkPointsArray);
        return jsonObject.toJSONString();
    }
}
