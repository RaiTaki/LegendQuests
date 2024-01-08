package xyz.raitaki.legendquests.questhandlers;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointType;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardType;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuestReward;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.ConversationPlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.InteractionPlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.KillPlayerCheckpoint;

import java.util.LinkedList;

public class QuestManager {

    private LinkedList<QuestBase> quests = new LinkedList<>();
    private LinkedList<QuestPlayer> questPlayers = new LinkedList<>();

    public QuestBase loadBaseQuestFromJSON(String questJson) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(questJson);
        QuestBase quest = new QuestBase((String) json.get("name"), (String) json.get("description"));

        JSONArray rewards = (JSONArray) json.get("rewards");
        for (Object reward : rewards) {
            JSONObject rewardJSON = (JSONObject) reward;
            String type = (String) rewardJSON.get("type");
            String value = (String) rewardJSON.get("value");
            quest.addReward(new QuestReward(quest, RewardType.valueOf(type), value));
        }

        JSONArray checkPoints = (JSONArray) json.get("checkPoints");
        for (Object checkPoint : checkPoints) {
            JSONObject checkPointJSON = (JSONObject) checkPoint;
            String type = (String) checkPointJSON.get("type");
            String value = (String) checkPointJSON.get("value");
            if(type.equals("KILL")) {
                int amount = Integer.parseInt((String) checkPointJSON.get("amount"));
                quest.addCheckPoint(new KillCheckpoint(quest, CheckPointType.valueOf(type), value, amount));
            }
            else if(type.equals("CONVERSATION")) {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckPoint(new ConversationCheckpoint(quest, CheckPointType.valueOf(type), value, npcName));
            }
            else {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckPoint(new InteractionCheckpoint(quest, CheckPointType.valueOf(type), value, npcName));
            }
        }

        return quest;
    }

    public PlayerQuest loadPlayerQuestFromJson(Player player, String questJson){
        JSONParser parser = new JSONParser();
        JSONObject json = null;
        try {
            json = (JSONObject) parser.parse(questJson);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String name = (String) json.get("name");
        String description = (String) json.get("description");
        PlayerQuest quest = new PlayerQuest(getQuestByName(name), getQuestPlayerFromPlayer(player));
        JSONArray rewards = (JSONArray) json.get("rewards");
        for (Object reward : rewards) {
            JSONObject rewardJSON = (JSONObject) reward;
            String type = (String) rewardJSON.get("type");
            String value = (String) rewardJSON.get("value");
            quest.addReward(new PlayerQuestReward(quest, PlayerQuestReward.RewardType.valueOf(type), value));
        }

        JSONArray checkPoints = (JSONArray) json.get("checkPoints");
        for (Object checkPoint : checkPoints) {
            JSONObject checkPointJSON = (JSONObject) checkPoint;
            String type = (String) checkPointJSON.get("type");
            String value = (String) checkPointJSON.get("value");
            Boolean completed = (Boolean) checkPointJSON.get("completed");
            if(type.equals("KILL")) {
                int amount = Integer.parseInt((String) checkPointJSON.get("amount"));
                int counter = Integer.parseInt((String) checkPointJSON.get("counter"));
                quest.addCheckpoint(new KillPlayerCheckpoint(quest, CheckPointType.valueOf(type), value, completed, amount, counter));
            }
            else if(type.equals("CONVERSATION")) {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckpoint(new ConversationPlayerCheckpoint(quest, CheckPointType.valueOf(type), completed, value, npcName));
            }
            else {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckpoint(new InteractionPlayerCheckpoint(quest, CheckPointType.valueOf(type), value, completed, npcName));
            }
        }
        return quest;
    }

    public void cloneBaseQuestToPlayer(Player player, QuestBase quest){
        QuestPlayer questPlayer = getQuestPlayerFromPlayer(player);
        PlayerQuest playerQuest = new PlayerQuest(quest, questPlayer);
        for (QuestReward reward : quest.getRewards()) {
            playerQuest.addReward(new PlayerQuestReward(playerQuest, PlayerQuestReward.RewardType.valueOf(reward.getType().toString()), reward.getValue()));
        }
        for (QuestCheckpoint checkPoint : quest.getCheckPoints()) {
            if(checkPoint.getType().equals(CheckPointType.KILL)) {
                KillCheckpoint killCheckpoint = (KillCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new KillPlayerCheckpoint(playerQuest, killCheckpoint.getType(), killCheckpoint.getValue(), false,  killCheckpoint.getAmount(), 0));
            }
            else if(checkPoint.getType().equals(CheckPointType.CONVERSATION)) {
                ConversationCheckpoint conversationCheckpoint = (ConversationCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new ConversationPlayerCheckpoint(playerQuest, conversationCheckpoint.getType(), false, conversationCheckpoint.getValue(), conversationCheckpoint.getNpcName()));
            }
            else {
                InteractionCheckpoint interactionCheckpoint = (InteractionCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new InteractionPlayerCheckpoint(playerQuest, interactionCheckpoint.getType(), interactionCheckpoint.getValue(), false, interactionCheckpoint.getNpcName()));
            }
        }
        questPlayer.addQuest(playerQuest);
    }

    public QuestBase getQuestByName(String name){
        for (QuestBase quest : quests) {
            if(quest.getName().equals(name)){
                return quest;
            }
        }
        return null;
    }

    public QuestPlayer getQuestPlayerFromPlayer(Player player){
        for (QuestPlayer questPlayer : questPlayers) {
            if(questPlayer.getPlayer().equals(player)){
                return questPlayer;
            }
        }
        return null;
    }
}
