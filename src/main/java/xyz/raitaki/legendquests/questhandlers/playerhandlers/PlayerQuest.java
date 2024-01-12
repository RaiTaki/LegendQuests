package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.events.PlayerNextCheckpointEvent;
import xyz.raitaki.legendquests.events.PlayerQuestEndEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestReward;

import java.util.LinkedList;

public class PlayerQuest {

    private QuestBase questBase;
    private QuestPlayer player;
    private PlayerCheckpoint checkPoint;
    private LinkedList<PlayerCheckpoint> checkpoints;
    private LinkedList<PlayerQuestReward> rewards;
    private boolean completed;
    private String questName;

    public PlayerQuest(QuestBase quest, QuestPlayer player) {
        this.questName = quest.getName();
        this.questBase = quest;
        this.player = player;
        this.checkpoints = new LinkedList<>();
        this.rewards = new LinkedList<>();
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
            PlayerNextCheckpointEvent event = new PlayerNextCheckpointEvent(player, this, checkPoint, checkpoints.get(index + 1));
            Bukkit.getPluginManager().callEvent(event);
            if(event.isCancelled()) {
                checkPoint = checkpoints.get(index);
                checkPoint.setCompleted(false);
            }
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

    public JSONObject getAsJSON(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", questBase.getName());
        jsonObject.put("description", questBase.getDescription());

        JSONArray rewardsArray = new JSONArray();
        for(PlayerQuestReward reward : rewards) {
            rewardsArray.add(reward.getAsJSON());
        }

        JSONArray checkPointsArray = new JSONArray();
        for(PlayerCheckpoint checkPoint : checkpoints) {
            checkPointsArray.add(checkPoint.getAsJSON());
        }

        jsonObject.put("rewards", rewardsArray);
        jsonObject.put("checkPoints", checkPointsArray);
        return jsonObject;
    }


}
