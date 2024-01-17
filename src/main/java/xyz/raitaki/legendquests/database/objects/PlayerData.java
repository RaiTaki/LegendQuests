package xyz.raitaki.legendquests.database.objects;

import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    for (Object quest : data) {
      JSONObject questData = (JSONObject) data.get(index);
      quests.add(new QuestData(questData));
      index++;
    }
    return quests;
  }
}
