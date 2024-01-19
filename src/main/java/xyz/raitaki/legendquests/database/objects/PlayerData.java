package xyz.raitaki.legendquests.database.objects;

import java.util.LinkedList;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.questhandlers.QuestManager;

public class PlayerData {

  private int id;
  private String uuid;
  private JSONArray data;

  public PlayerData(int id, String uuid, JSONArray data) {
    this.id = id;
    this.uuid = uuid;
    this.data = data;
  }

  public int getId() {
    return id;
  }

  public String getUuid() {
    return uuid;
  }

  public JSONArray getData() {
    return data;
  }

  public LinkedList<QuestData> getQuests() {
    LinkedList<QuestData> quests = new LinkedList<>();
    int index = 0;

    if (data.isEmpty()) {
      Player player = Bukkit.getPlayer(UUID.fromString(uuid));
      if (player == null) {
        return quests;
      }

      QuestManager.addBaseQuestToPlayer(player.getPlayer(), QuestManager.getBaseQuests().get(0));
      return quests;
    }
    for (Object quest : data) {
      JSONObject questData = (JSONObject) data.get(index);
      quests.add(new QuestData(questData, true));
      index++;
    }
    return quests;
  }
}
