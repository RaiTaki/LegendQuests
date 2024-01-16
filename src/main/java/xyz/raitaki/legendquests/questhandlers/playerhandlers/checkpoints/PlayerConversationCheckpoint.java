package xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.events.PlayerAnswerEvent;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerCheckpoint;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.utils.TextUtils;

public class PlayerConversationCheckpoint extends PlayerCheckpoint {

  private String npcName;
  private String acceptText;
  private String declineText;
  private AnswerEnum answer;

  public PlayerConversationCheckpoint(PlayerQuest quest, CheckPointTypeEnum type, boolean completed,
      String text, String npcName, String acceptText, String declineText) {
    super(quest, type, text, completed);
    this.npcName = npcName;
    this.acceptText = acceptText;
    this.declineText = declineText;
    answer = AnswerEnum.ACCEPT;
  }

  /**
   * send conversation message to the player
   */
  public void sendMessage() {
    Player player = getQuest().getQuestPlayer().getPlayer();
    TextUtils.sendCenteredMessage(player,
        "&m                                                                    ");
    TextUtils.sendCenteredMessage(player, "&a&l" + npcName);
    TextUtils.sendCenteredMessage(player, "<SOLID:7d7d7d> " + getValue());
    if (answer == AnswerEnum.ACCEPT) {
      TextUtils.sendCenteredMessage(player, "&a&l " + acceptText + "      &c" + declineText);
    } else {
      TextUtils.sendCenteredMessage(player, "&c " + acceptText + "      &a&l" + declineText);
    }
    TextUtils.sendCenteredMessage(player,
        "&m                                                                    ");

    if (acceptText.isEmpty() || declineText.isEmpty()) {
      doAnswer();
    }
  }

  /**
   * @return the name of the npc
   */
  public String getNpcName() {
    return npcName;
  }

  /**
   * @return JSON representation of the checkpoint
   */
  @Override
  public JSONObject getAsJSON() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", getType().toString());
    jsonObject.put("value", getValue());
    jsonObject.put("npcName", getNpcName());
    jsonObject.put("completed", isCompleted());
    jsonObject.put("acceptText", getAcceptText());
    jsonObject.put("declineText", getDeclineText());
    return jsonObject;
  }

  /**
   * does the answer based on the answer type
   */
  public void doAnswer() {
    Player player = getQuest().getQuestPlayer().getPlayer();
    if (answer == AnswerEnum.ACCEPT) {
      PlayerAnswerEvent event = new PlayerAnswerEvent(getQuest().getQuestPlayer(), getQuest());
      getQuest().getQuestPlayer().getPlayer().getServer().getPluginManager().callEvent(event);
      if (acceptText.isEmpty() || declineText.isEmpty()) {
        return;
      }
      TextUtils.sendCenteredMessage(player,
          "&m                                                                    ");
      TextUtils.sendCenteredMessage(player, "&aYou accepted the quest!");
      TextUtils.sendCenteredMessage(player,
          "&m                                                                    ");
    } else {
      getQuest().previousCheckPoint();
      TextUtils.sendCenteredMessage(player,
          "&m                                                                    ");
      TextUtils.sendCenteredMessage(player, "&cYou declined the quest!");
      TextUtils.sendCenteredMessage(player,
          "&m                                                                    ");
    }
  }

  /**
   * @return the accept text of the checkpoint
   */
  public String getAcceptText() {
    return acceptText;
  }

  /**
   * @return the decline text of the checkpoint
   */
  public String getDeclineText() {
    return declineText;
  }

  /**
   * change the answer of the checkpoint
   */
  public void changeAnswer() {
    answer = answer == AnswerEnum.ACCEPT ? AnswerEnum.DECLINE : AnswerEnum.ACCEPT;
    sendMessage();
  }

  public enum AnswerEnum {
    ACCEPT,
    DECLINE
  }
}
