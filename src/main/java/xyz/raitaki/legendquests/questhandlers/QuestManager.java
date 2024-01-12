package xyz.raitaki.legendquests.questhandlers;

import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.listeners.custom.*;
import xyz.raitaki.legendquests.events.listeners.vanilla.EntityDamageEventListener;
import xyz.raitaki.legendquests.events.listeners.vanilla.PlayerEventListener;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointType;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardType;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuestReward;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerInteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerKillCheckpoint;

import java.util.HashMap;
import java.util.LinkedList;

public class QuestManager {

    private static LinkedList<QuestBase> quests = new LinkedList<>();
    private static HashMap<Player, QuestPlayer> questPlayers = new HashMap<>();

    public static QuestBase loadBaseQuestFromJSON(String questJson) throws ParseException {
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

    public static PlayerQuest loadPlayerQuestFromJson(Player player, String questJson){
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
                quest.addCheckpoint(new PlayerKillCheckpoint(quest, CheckPointType.valueOf(type), value, completed, amount, counter));
            }
            else if(type.equals("CONVERSATION")) {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckpoint(new PlayerConversationCheckpoint(quest, CheckPointType.valueOf(type), completed, value, npcName));
            }
            else {
                String npcName = (String) checkPointJSON.get("npcName");
                quest.addCheckpoint(new PlayerInteractionCheckpoint(quest, CheckPointType.valueOf(type), value, completed, npcName));
            }
        }
        updatePlayerQuest(quest);
        return quest;
    }

    public static void addBaseQuestToPlayer(Player player, QuestBase quest){
        QuestPlayer questPlayer = getQuestPlayerFromPlayer(player);
        PlayerQuest playerQuest = new PlayerQuest(quest, questPlayer);
        for (QuestReward reward : quest.getRewards()) {
            playerQuest.addReward(new PlayerQuestReward(playerQuest, PlayerQuestReward.RewardType.valueOf(reward.getType().toString()), reward.getValue()));
        }
        for (QuestCheckpoint checkPoint : quest.getCheckPoints()) {
            if(checkPoint.getType().equals(CheckPointType.KILL)) {
                KillCheckpoint killCheckpoint = (KillCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new PlayerKillCheckpoint(playerQuest, killCheckpoint.getType(), killCheckpoint.getValue(), false,  killCheckpoint.getAmount(), 0));
            }
            else if(checkPoint.getType().equals(CheckPointType.CONVERSATION)) {
                ConversationCheckpoint conversationCheckpoint = (ConversationCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new PlayerConversationCheckpoint(playerQuest, conversationCheckpoint.getType(), false, conversationCheckpoint.getValue(), conversationCheckpoint.getNpcName()));
            }
            else {
                InteractionCheckpoint interactionCheckpoint = (InteractionCheckpoint) checkPoint;
                playerQuest.addCheckpoint(new PlayerInteractionCheckpoint(playerQuest, interactionCheckpoint.getType(), interactionCheckpoint.getValue(), false, interactionCheckpoint.getNpcName()));
            }
        }
        questPlayer.addQuest(playerQuest);
        playerQuest.setCheckPoint(playerQuest.getCheckpoints().getFirst());
    }

    public static QuestBase getQuestByName(String name){
        for (QuestBase quest : quests) {
            if(quest.getName().equals(name)){
                return quest;
            }
        }
        return null;
    }

    public static QuestPlayer getQuestPlayerFromPlayer(Player player){
        if (!questPlayers.containsKey(player)) {
            createQuestPlayer(player);
        }
        return questPlayers.get(player);
    }

    public static void createQuestPlayer(Player player){
        //GET PLAYER DATA FROM DATABASE
        //CREATE QUEST PLAYER
        //ADD TO questPlayers

        questPlayers.put(player, new QuestPlayer(player));
    }

    public static void saveQuestPlayer(QuestPlayer questPlayer){
        //SAVE PLAYER DATA TO DATABASE
    }

    public static LinkedList<QuestBase> getBaseQuests(){
        return quests;
    }

    public static void registerEvents(){
        LegendQuests instance = LegendQuests.getInstance();

        //CUSTOM EVENTS
        instance.getServer().getPluginManager().registerEvents(new EntityKillListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new PlayerCheckpointListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new PlayerQuestEndListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new PlayerQuestInteractListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new PlayerQuestKillListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new QuestUpdateListener(), instance);

        //VANILLA EVENTS
        instance.getServer().getPluginManager().registerEvents(new EntityDamageEventListener(), instance);
        instance.getServer().getPluginManager().registerEvents(new PlayerEventListener(), instance);
    }

    public static QuestBase getQuestBaseFromEditor(Player player){
        for (QuestBase quest : quests) {
            if(quest.getQuestGUI().getEditor() != null && quest.getQuestGUI().getEditor().equals(player)){
                return quest;
            }
        }

        return null;
    }

    public static void updatePlayerQuests(QuestPlayer questPlayer){
        for (PlayerQuest playerQuest : questPlayer.getQuests()) {
            updatePlayerQuest(playerQuest);
        }
    }

    public static void updatePlayerQuest(PlayerQuest playerQuest){
        for(PlayerCheckpoint playerCheckpoint : playerQuest.getCheckpoints()){
            if(playerCheckpoint.isCompleted()) continue;

            QuestBase questBase = getQuestByName(playerQuest.getQuestName());
            int index = playerQuest.getCheckpoints().indexOf(playerCheckpoint);
            QuestCheckpoint questCheckpoint = questBase.getCheckPoints().get(index);
            PlayerCheckpoint playerCheckpointClone = clonePlayerFromBaseChecpoint(playerQuest, questCheckpoint);
            playerQuest.getCheckpoints().set(index, playerCheckpointClone);
        }
    }

    public static void updatePlayerQuestBaseQuest(QuestPlayer player, QuestBase questBase){
        PlayerQuest playerQuest = player.getPlayerQuestByQuestName(questBase.getName());
        if(playerQuest == null) return;
        for(PlayerCheckpoint playerCheckpoint : playerQuest.getCheckpoints()){
            if(playerCheckpoint.isCompleted()) continue;

            int index = playerQuest.getCheckpoints().indexOf(playerCheckpoint);
            QuestCheckpoint questCheckpoint = questBase.getCheckPoints().get(index);
            PlayerCheckpoint playerCheckpointClone = clonePlayerFromBaseChecpoint(playerQuest, questCheckpoint);
            playerQuest.getCheckpoints().set(index, playerCheckpointClone);
        }
    }

    public static void updatePlayerQuestByName(QuestPlayer player, String questName){
        PlayerQuest playerQuest = player.getPlayerQuestByQuestName(questName);
        updatePlayerQuest(playerQuest);
    }

    public static PlayerCheckpoint clonePlayerFromBaseChecpoint(PlayerQuest quest, QuestCheckpoint checkpoint){
        PlayerCheckpoint playerCheckpoint = null;
        if(checkpoint.getType().equals(CheckPointType.KILL)){
            KillCheckpoint killCheckpoint = (KillCheckpoint) checkpoint;
            playerCheckpoint = new PlayerKillCheckpoint(quest, killCheckpoint.getType(), killCheckpoint.getValue(), false, killCheckpoint.getAmount(), 0);
        }
        else if(checkpoint.getType().equals(CheckPointType.CONVERSATION)){
            ConversationCheckpoint conversationCheckpoint = (ConversationCheckpoint) checkpoint;
            playerCheckpoint = new PlayerConversationCheckpoint(quest, conversationCheckpoint.getType(), false, conversationCheckpoint.getValue(), conversationCheckpoint.getNpcName());
        }
        else {
            InteractionCheckpoint interactionCheckpoint = (InteractionCheckpoint) checkpoint;
            playerCheckpoint = new PlayerInteractionCheckpoint(quest, interactionCheckpoint.getType(), interactionCheckpoint.getValue(), false, interactionCheckpoint.getNpcName());
        }
        return playerCheckpoint;
    }

    public static HashMap<Player, QuestPlayer> getQuestPlayers() {
        return questPlayers;
    }
}
