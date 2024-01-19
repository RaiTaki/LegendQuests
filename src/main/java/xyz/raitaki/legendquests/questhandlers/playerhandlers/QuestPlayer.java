package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import java.util.LinkedList;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.utils.PacketDisplay;
import xyz.raitaki.legendquests.utils.TextUtils;

public class QuestPlayer {

  private Player player;
  private UUID uuid;
  private LinkedList<PlayerQuest> quests;
  private PacketDisplay packetDisplay;

  public QuestPlayer(Player player) {
    this.player = player;
    this.uuid = player.getUniqueId();
    quests = new LinkedList<>();

    packetDisplay = new PacketDisplay(player,
        "%legendquest_questname%\n%legendquest_questdescription%\n%legendquest_checkpoint%\n%legendquest_remainingtime%");
  }

  /**
   * @param quest add the quest to the player
   */
  public void addQuest(PlayerQuest quest) {
    quests.add(quest);
  }

  /**
   * @return the quests of the player
   */
  public LinkedList<PlayerQuest> getQuests() {
    return quests;
  }

  public PlayerQuest getQuestOnGoing() {
    for (PlayerQuest quest : quests) {
      if (!quest.isCompleted()) {
        return quest;
      }
    }
    return null;
  }

  /**
   * @return the player
   */
  public Player getPlayer() {
    return player;
  }

  /**
   * @param type the type of the checkpoint
   * @return the quest of the player by the checkpoint type
   */
  public PlayerQuest getPlayerQuestByCheckpointType(QuestCheckpoint.CheckPointTypeEnum type) {
    for (PlayerQuest quest : quests) {
      PlayerCheckpoint checkpoint = quest.getCheckpoint();
      if (checkpoint.getType().equals(type) && !quest.isCompleted() && !checkpoint.isCompleted()) {
        return quest;
      }
    }
    return null;
  }

  /**
   * @param questBase the quest base of the quest
   * @return the quest of the player by the quest base
   */
  public PlayerQuest getPlayerQuestByQuestBase(QuestBase questBase) {
    for (PlayerQuest quest : quests) {
      if (quest.getQuest().equals(questBase)) {
        return quest;
      }
    }
    return null;
  }

  /**
   * @param questName the name of the quest
   * @return the quest of the player by the quest name
   */
  public PlayerQuest getPlayerQuestByQuestName(String questName) {
    for (PlayerQuest quest : quests) {
      if (quest.getQuestName().equals(questName)) {
        return quest;
      }
    }
    return null;
  }

  /**
   * Send the quest info to the player
   */
  public void sendQuestInfoChat() {
    PlayerQuest quest = getQuestOnGoing();
    TextUtils.sendCenteredMessage(player, "&8&m                                         ");

    if (quest == null) {
      TextUtils.sendCenteredMessage(player, "&c&lNo quest on going");
      TextUtils.sendCenteredMessage(player, "&8&m                                         ");
      return;
    } else {
      TextUtils.sendCenteredMessage(player, "&a&l" + quest.getQuestName());
      TextUtils.sendCenteredMessage(player, "&7" + quest.getDescription());
      TextUtils.sendCenteredMessage(player,
          "&7" + PlaceholderAPI.setPlaceholders(player, "%legendquest_checkpoint%"));
      TextUtils.sendCenteredMessage(player,
          "&7 Remaining time: " + quest.getRemainingTimeFormatted());
    }
    TextUtils.sendCenteredMessage(player, "&8&m                                         ");
  }

  /**
   * @param packetDisplay the packet display of the player
   */
  public void setPacketDisplay(PacketDisplay packetDisplay) {
    this.packetDisplay = packetDisplay;
  }

  /**
   * @return the packet display of the player
   */
  public PacketDisplay getPacketDisplay() {
    return packetDisplay;
  }

  /**
   * @return the uuid of the player
   */
  public UUID getUuid() {
    return uuid;
  }

  public JSONArray getAsJSON() {
    JSONArray jsonArray = new JSONArray();
    for (PlayerQuest quest : quests) {
      jsonArray.add(quest.getAsJSON());
    }
    return jsonArray;
  }
}
