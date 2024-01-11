package xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints;

import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;

public class PlayerKillCheckpoint extends PlayerCheckpoint {

    int counter = 0;
    int amount = 0;
    public PlayerKillCheckpoint(PlayerQuest quest, QuestCheckpoint.CheckPointType type, String targetName, boolean completed, int amount, int counter) {
        super(quest, type, targetName, completed);
        this.amount = amount;
        this.counter = counter;
    }

    public void incrementProgress(){
        counter++;
    }

    public int getCounter() {
        return counter;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isComplete(){
        return counter >= amount;
    }

    @Override
    public JSONObject getAsJSON() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", getType().toString());
        jsonObject.put("value", getValue());
        jsonObject.put("amount", amount);
        jsonObject.put("counter", counter);
        jsonObject.put("completed", isCompleted());
        return jsonObject;
    }
}