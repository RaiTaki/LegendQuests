package xyz.raitaki.legendquests.questhandlers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.database.objects.CheckpointData;
import xyz.raitaki.legendquests.database.objects.PlayerData;
import xyz.raitaki.legendquests.database.objects.QuestData;
import xyz.raitaki.legendquests.database.objects.RewardData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.ConversationCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.InteractionCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.KillCheckpointData;
import xyz.raitaki.legendquests.events.listeners.custom.EntityKillListener;
import xyz.raitaki.legendquests.events.listeners.custom.PlayerAnswerListener;
import xyz.raitaki.legendquests.events.listeners.custom.PlayerCheckpointListener;
import xyz.raitaki.legendquests.events.listeners.custom.PlayerQuestEndListener;
import xyz.raitaki.legendquests.events.listeners.custom.PlayerQuestInteractListener;
import xyz.raitaki.legendquests.events.listeners.custom.PlayerQuestKillListener;
import xyz.raitaki.legendquests.events.listeners.custom.QuestUpdateListener;
import xyz.raitaki.legendquests.events.listeners.vanilla.EntityDamageEventListener;
import xyz.raitaki.legendquests.events.listeners.vanilla.PlayerEventListener;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
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

public class QuestManager {

  private static LinkedList<QuestBase> quests = new LinkedList<>();
  private static HashMap<Player, QuestPlayer> questPlayers = new HashMap<>();

  /**
   * This method loads a QuestBase from JSON
   *
   * @param questJson - Quest as JSON
   * @return QuestBase - QuestBase from JSON
   */
  public static QuestBase loadBaseQuestFromJSON(String questJson) throws ParseException {
    JSONParser parser = new JSONParser();
    JSONObject json = (JSONObject) parser.parse(questJson);

    String name = (String) json.get("name");
    String description = (String) json.get("description");
    Long time = (Long) json.get("time");
    QuestBase quest = new QuestBase(name, description, time);

    JSONArray rewards = (JSONArray) json.get("rewards");
    for (Object reward : rewards) {
      JSONObject rewardJSON = (JSONObject) reward;
      String type = (String) rewardJSON.get("type");
      String value = (String) rewardJSON.get("value");
      quest.addReward(new QuestReward(quest, QuestReward.RewardTypeEnum.valueOf(type), value));
    }

    JSONArray checkPoints = (JSONArray) json.get("checkPoints");
    for (Object checkPoint : checkPoints) {
      JSONObject checkPointJSON = (JSONObject) checkPoint;
      String type = (String) checkPointJSON.get("type");
      String value = (String) checkPointJSON.get("value");
      if (type.equals("KILL")) {
        int amount = Integer.parseInt((String) checkPointJSON.get("amount"));
        quest.addCheckPoint(
            new KillCheckpoint(quest, CheckPointTypeEnum.valueOf(type), value, amount));
      } else if (type.equals("CONVERSATION")) {
        String npcName = (String) checkPointJSON.get("npcName");
        String acceptText = (String) checkPointJSON.get("acceptText");
        String declineText = (String) checkPointJSON.get("declineText");
        quest.addCheckPoint(
            new ConversationCheckpoint(quest, CheckPointTypeEnum.valueOf(type), value, npcName,
                acceptText, declineText));
      } else {
        String npcName = (String) checkPointJSON.get("npcName");
        quest.addCheckPoint(
            new InteractionCheckpoint(quest, CheckPointTypeEnum.valueOf(type), value, npcName));
      }
    }

    return quest;
  }

  /**
   * This method loads PlayerQuest from QuestData
   *
   * @param player - Player
   * @param data   - QuestData
   */
  public static void loadPlayerQuestFromData(Player player, QuestData data) {
    QuestPlayer questPlayer = getQuestPlayerByPlayer(player);

    QuestBase questBase = getQuestBaseByName(data.getName());
    if (questBase == null) {
      return;
    }
    PlayerQuest quest = new PlayerQuest(questBase, questPlayer, data.getTime());

    try {
      for (RewardData reward : data.getRewards()) {
        quest.addReward(new PlayerQuestReward(quest, reward.getType(), reward.getValue()));
      }
    } catch (Exception e) {
    }

    for (CheckpointData checkpoint : data.getCheckpoints()) {
      if (checkpoint.getType().equals(CheckPointTypeEnum.KILL)) {
        KillCheckpointData killCheckpoint = (KillCheckpointData) checkpoint;
        quest.addCheckpoint(new PlayerKillCheckpoint(quest, checkpoint.getType(),
            checkpoint.getValue(), checkpoint.isCompleted(), killCheckpoint.getAmount(),
            killCheckpoint.getCounter()));
      } else if (checkpoint.getType().equals(CheckPointTypeEnum.CONVERSATION)) {
        ConversationCheckpointData conversationCheckpoint = (ConversationCheckpointData) checkpoint;
        quest.addCheckpoint(new PlayerConversationCheckpoint(quest, checkpoint.getType(),
            checkpoint.isCompleted(), checkpoint.getValue(), conversationCheckpoint.getNpcName(),
            conversationCheckpoint.getAcceptText(), conversationCheckpoint.getDeclineText()));
      } else if (checkpoint.getType().equals(CheckPointTypeEnum.INTERACT)) {
        InteractionCheckpointData interactionCheckpoint = (InteractionCheckpointData) checkpoint;
        quest.addCheckpoint(new PlayerInteractionCheckpoint(quest, checkpoint.getType(),
            checkpoint.getValue(), checkpoint.isCompleted(), interactionCheckpoint.getNpcName()));
      }
    }

    quest.setCompleted(data.isCompleted());
    updatePlayerQuest(quest);
    questPlayer.addQuest(quest);
  }

  /**
   * This method clones QuestBase to PlayerQuest
   *
   * @param player - Player
   * @param quest  - QuestBase
   */
  public static void addBaseQuestToPlayer(Player player, QuestBase quest) {
    QuestPlayer questPlayer = getQuestPlayerByPlayer(player);
    PlayerQuest playerQuest = new PlayerQuest(quest, questPlayer, quest.getTime());
    for (QuestReward reward : quest.getRewards()) {
      playerQuest.addReward(
          new PlayerQuestReward(playerQuest, RewardTypeEnum.valueOf(reward.getType().toString()),
              reward.getValue()));
    }
    for (QuestCheckpoint checkPoint : quest.getCheckPoints()) {
      if (checkPoint.getType().equals(CheckPointTypeEnum.KILL)) {
        KillCheckpoint killCheckpoint = (KillCheckpoint) checkPoint;
        playerQuest.addCheckpoint(new PlayerKillCheckpoint(playerQuest,
            killCheckpoint.getType(),
            killCheckpoint.getValue(),
            false,
            killCheckpoint.getAmount(),
            0));
      } else if (checkPoint.getType().equals(CheckPointTypeEnum.CONVERSATION)) {
        ConversationCheckpoint conversationCheckpoint = (ConversationCheckpoint) checkPoint;
        playerQuest.addCheckpoint(new PlayerConversationCheckpoint(playerQuest,
            conversationCheckpoint.getType(),
            false,
            conversationCheckpoint.getValue(),
            conversationCheckpoint.getNpcName(),
            conversationCheckpoint.getAcceptText(),
            conversationCheckpoint.getDeclineText()));
      } else {
        InteractionCheckpoint interactionCheckpoint = (InteractionCheckpoint) checkPoint;
        playerQuest.addCheckpoint(new PlayerInteractionCheckpoint(playerQuest,
            interactionCheckpoint.getType(),
            interactionCheckpoint.getValue(),
            false,
            interactionCheckpoint.getNpcName()));
      }
    }
    questPlayer.addQuest(playerQuest);
    playerQuest.setCheckPoint(playerQuest.getCheckpoints().getFirst());
  }

  /**
   * This method gets QuestBase by name
   *
   * @param name - Quest name
   * @return QuestBase - QuestBase by name
   */
  public static QuestBase getQuestBaseByName(String name) {
    for (QuestBase quest : quests) {
      if (quest.getName().equals(name)) {
        return quest;
      }
    }
    return null;
  }

  /**
   * This method gets QuestPlayer by Player
   *
   * @param player - Player
   * @return QuestPlayer - QuestPlayer by Player
   */
  public static QuestPlayer getQuestPlayerByPlayer(Player player) {
    if (!questPlayers.containsKey(player)) {
      createQuestPlayer(player);
    }
    return questPlayers.get(player);
  }

  /**
   * This method gets QuestPlayer by UUID
   *
   * @param uuid - UUID
   * @return QuestPlayer - QuestPlayer by UUID
   */
  public static QuestPlayer getQuestPlayerByUUID(String uuid) {
    UUID uuidParsed = UUID.fromString(uuid);
    Player player = Bukkit.getPlayer(uuidParsed);
    if (player != null) {
      return getQuestPlayerByPlayer(player);
    }
    return null;
  }

  /**
   * This method creates QuestPlayer
   *
   * @param player - Player
   */
  public static void createQuestPlayer(Player player) {
    //TODO: GET PLAYER DATA FROM DATABASE
    //TODO: CREATE QUEST PLAYER
    //TODO: ADD TO questPlayers

    questPlayers.put(player, new QuestPlayer(player));
  }

  /**
   * This method saves QuestPlayer
   *
   * @param questPlayer - QuestPlayer
   */
  public static void saveQuestPlayer(QuestPlayer questPlayer) {
    //TODO: SAVE PLAYER DATA TO DATABASE
  }

  /**
   * This method returns all QuestBases
   *
   * @return LinkedList<QuestBase> - All QuestBases
   */
  public static LinkedList<QuestBase> getBaseQuests() {
    return quests;
  }

  /**
   * This method registers all events
   */
  public static void registerEvents() {
    LegendQuests instance = LegendQuests.getInstance();

    //CUSTOM EVENTS
    instance.getServer().getPluginManager().registerEvents(new EntityKillListener(), instance);
    instance.getServer().getPluginManager()
        .registerEvents(new PlayerCheckpointListener(), instance);
    instance.getServer().getPluginManager().registerEvents(new PlayerQuestEndListener(), instance);
    instance.getServer().getPluginManager()
        .registerEvents(new PlayerQuestInteractListener(), instance);
    instance.getServer().getPluginManager()
        .registerEvents(new PlayerQuestKillListener(), instance);
    instance.getServer().getPluginManager().registerEvents(new QuestUpdateListener(), instance);
    instance.getServer().getPluginManager().registerEvents(new PlayerAnswerListener(), instance);

    //VANILLA EVENTS
    instance.getServer().getPluginManager()
        .registerEvents(new EntityDamageEventListener(), instance);
    instance.getServer().getPluginManager().registerEvents(new PlayerEventListener(), instance);
  }

  /**
   * This method returns QuestBase by Player
   *
   * @param player - Player
   * @return QuestBase - QuestBase by Player
   */
  public static QuestBase getQuestBaseFromEditor(Player player) {
    for (QuestBase quest : quests) {
      if (quest.getQuestGUI().getEditor() != null && quest.getQuestGUI().getEditor()
          .equals(player)) {
        return quest;
      }
    }

    return null;
  }

  /**
   * This method updates Player's Quests
   *
   * @param questPlayer - QuestPlayer
   */
  public static void updatePlayerQuests(QuestPlayer questPlayer) {
    for (PlayerQuest playerQuest : questPlayer.getQuests()) {
      updatePlayerQuest(playerQuest);
    }
  }

  /**
   * This method updates Player's Quest
   *
   * @param playerQuest - PlayerQuest
   */
  public static void updatePlayerQuest(PlayerQuest playerQuest) {
    if (playerQuest == null) {
      return;
    }
    QuestBase questBase = getQuestBaseByName(playerQuest.getQuestName());

    if (questBase == null) {
      return;
    }
    if (playerQuest.isCompleted()) {
      return;
    }
    for (QuestCheckpoint questCheckpoint : questBase.getCheckPoints()) {
      int index = questBase.getCheckPoints().indexOf(questCheckpoint);

      if (index >= playerQuest.getCheckpoints().size()) {
        PlayerCheckpoint playerCheckpoint = clonePlayerFromBase(playerQuest,
            questCheckpoint, null);
        playerQuest.getCheckpoints().add(playerCheckpoint);
      }
      PlayerCheckpoint playerCheckpoint = playerQuest.getCheckpoints().get(index);

      if (playerCheckpoint == null) {
        continue;
      }
      if (playerCheckpoint.isCompleted()) {
        continue;
      }
      playerCheckpoint = clonePlayerFromBase(playerQuest, questCheckpoint, playerCheckpoint);
      playerQuest.getCheckpoints().set(index, playerCheckpoint);
    }
    playerQuest.updateCheckpoint();
  }

  /**
   * This method updates Player's Quest by QuestBase
   *
   * @param player    - Player
   * @param questBase - QuestBase
   */
  public static void updatePlayerQuestBaseQuest(QuestPlayer player, QuestBase questBase) {
    PlayerQuest playerQuest = player.getPlayerQuestByQuestBase(questBase);
    updatePlayerQuest(playerQuest);
  }

  /**
   * This method updates Player's Quest by name
   *
   * @param player    - Player
   * @param questName - Quest name
   */
  public static void updatePlayerQuestByName(QuestPlayer player, String questName) {
    PlayerQuest playerQuest = player.getPlayerQuestByQuestName(questName);
    updatePlayerQuest(playerQuest);
  }

  /**
   * This method clones PlayerCheckpoint from QuestCheckpoint
   *
   * @param quest      - PlayerQuest
   * @param checkpoint - QuestCheckpoint
   * @return PlayerCheckpoint - PlayerCheckpoint from QuestCheckpoint
   */
  public static PlayerCheckpoint clonePlayerFromBase(PlayerQuest quest,
      QuestCheckpoint checkpoint, PlayerCheckpoint playerCheckpoint) {
    if (checkpoint.getType().equals(CheckPointTypeEnum.KILL)) {
      KillCheckpoint killCheckpoint = (KillCheckpoint) checkpoint;
      PlayerKillCheckpoint playerKillCheckpoint = (PlayerKillCheckpoint) playerCheckpoint;

      if (playerKillCheckpoint != null) {
        playerCheckpoint = new PlayerKillCheckpoint(quest, killCheckpoint.getType(),
            killCheckpoint.getValue(), false, killCheckpoint.getAmount(),
            playerKillCheckpoint.getCounter());
      } else {
        playerCheckpoint = new PlayerKillCheckpoint(quest, killCheckpoint.getType(),
            killCheckpoint.getValue(), false, killCheckpoint.getAmount(), 0);
      }
    } else if (checkpoint.getType().equals(CheckPointTypeEnum.CONVERSATION)) {
      ConversationCheckpoint conversationCheckpoint = (ConversationCheckpoint) checkpoint;

      playerCheckpoint = new PlayerConversationCheckpoint(quest, conversationCheckpoint.getType(),
          false, conversationCheckpoint.getValue(), conversationCheckpoint.getNpcName(),
          conversationCheckpoint.getAcceptText(), conversationCheckpoint.getDeclineText());
    } else {
      InteractionCheckpoint interactionCheckpoint = (InteractionCheckpoint) checkpoint;

      playerCheckpoint = new PlayerInteractionCheckpoint(quest, interactionCheckpoint.getType(),
          interactionCheckpoint.getValue(), false, interactionCheckpoint.getNpcName());
    }
    return playerCheckpoint;
  }

  /**
   * This method removes QuestPlayer
   *
   * @param player - Player
   */
  public static void removeQuestPlayer(Player player) {
    questPlayers.remove(player);
  }

  /**
   * This method updates all Player's Quests
   */
  public static void updatePlayersQuest() {
    for (QuestPlayer questPlayer : questPlayers.values()) {
      updatePlayerQuests(questPlayer);
      Bukkit.broadcastMessage(questPlayer.getQuests().toString());
    }
  }

  /**
   * This method returns all QuestPlayers
   *
   * @return HashMap<Player, QuestPlayer> - All QuestPlayers
   */
  public static HashMap<Player, QuestPlayer> getQuestPlayers() {
    return questPlayers;
  }
}
