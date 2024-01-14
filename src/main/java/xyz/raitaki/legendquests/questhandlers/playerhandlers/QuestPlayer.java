package xyz.raitaki.legendquests.questhandlers.playerhandlers;

import java.util.LinkedList;
import java.util.UUID;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.utils.TextUtils;

public class QuestPlayer {

  private Player player;
  private UUID uuid;
  private LinkedList<PlayerQuest> quests;

  public QuestPlayer(Player player) {
    this.player = player;
    this.uuid = player.getUniqueId();
    quests = new LinkedList<>();
  }

  public void addQuest(PlayerQuest quest) {
    quests.add(quest);
  }

  public LinkedList<PlayerQuest> getQuests() {
    return quests;
  }

  public Player getPlayer() {
    return player;
  }

  public PlayerQuest getPlayerQuestByCheckpointType(QuestCheckpoint.CheckPointTypeEnum type) {
    for (PlayerQuest quest : quests) {
      if (quest.getCheckPoint().getType().equals(type)) {
        return quest;
      }
    }
    return null;
  }

  public PlayerQuest getPlayerQuestByQuestBase(QuestBase questBase) {
    for (PlayerQuest quest : quests) {
      if (quest.getQuest().equals(questBase)) {
        return quest;
      }
    }
    return null;
  }

  public PlayerQuest getPlayerQuestByQuestName(String questName) {
    for (PlayerQuest quest : quests) {
      if (quest.getQuestName().equals(questName)) {
        return quest;
      }
    }
    return null;
  }

  public void sendQuestInfoChat() {
    for (PlayerQuest quest : quests) {
      TextUtils.sendCenteredMessage(player, "&8&m                                         ");
      TextUtils.sendCenteredMessage(player, "&a&l" + quest.getQuestName());
      TextUtils.sendCenteredMessage(player, "&7" + quest.getDescription());
      TextUtils.sendCenteredMessage(player,
          "&7" + PlaceholderAPI.setPlaceholders(player, "%legendquest_checkpoint%"));
      TextUtils.sendCenteredMessage(player, "&7 Remaining time: " + quest.getRemainingTimeFormatted());
      TextUtils.sendCenteredMessage(player, "&8&m                                         ");
    }
  }
}
