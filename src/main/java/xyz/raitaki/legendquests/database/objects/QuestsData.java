package xyz.raitaki.legendquests.database.objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.ConversationCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.InteractionCheckpointData;
import xyz.raitaki.legendquests.database.objects.checkpointtypes.KillCheckpointData;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;

public class QuestsData {

  private JSONArray data;

  public QuestsData(JSONArray data) {
    this.data = data;
    loadQuests();
  }

  private void loadQuests() {
    int index = 0;
    for (Object quest : data) {
      JSONObject questDataJson = (JSONObject) data.get(index);
      QuestData questData = new QuestData(questDataJson, false);

      String questName = questData.getName();
      String questId = questData.getId();
      String questDescription = questData.getDescription();
      String nextQuestName = questData.getNextQuestName();
      long questTime = questData.getTime();

      QuestBase questBase = new QuestBase(questId, questName, questDescription, questTime,
          nextQuestName);

      for (CheckpointData checkpointData : questData.getCheckpoints()) {
        CheckPointTypeEnum checkpointType = checkpointData.getType();
        String checkpointValue = checkpointData.getValue();

        switch (checkpointType) {
          case KILL:
            KillCheckpointData killCheckpointData = (KillCheckpointData) checkpointData;
            KillCheckpoint killCheckpoint = new KillCheckpoint(questBase, CheckPointTypeEnum.KILL,
                checkpointValue, killCheckpointData.getAmount());
            questBase.addCheckpoint(killCheckpoint);
            break;
          case INTERACT:
            InteractionCheckpointData interactionCheckpointData = (InteractionCheckpointData) checkpointData;
            InteractionCheckpoint interactionCheckpoint = new InteractionCheckpoint(questBase,
                CheckPointTypeEnum.INTERACT, interactionCheckpointData.getValue(),
                interactionCheckpointData.getNpcName());
            questBase.addCheckpoint(interactionCheckpoint);
            break;
          case CONVERSATION:
            ConversationCheckpointData conversationCheckpointData = (ConversationCheckpointData) checkpointData;
            ConversationCheckpoint conversationCheckpoint = new ConversationCheckpoint(questBase,
                CheckPointTypeEnum.CONVERSATION, conversationCheckpointData.getValue(),
                conversationCheckpointData.getNpcName(), conversationCheckpointData.getAcceptText(),
                conversationCheckpointData.getDeclineText());
            questBase.addCheckpoint(conversationCheckpoint);
            break;
        }
      }

      for (RewardData rewardData : questData.getRewards()) {
        RewardTypeEnum rewardType = rewardData.getType();
        String rewardValue = rewardData.getValue();

        questBase.addReward(new QuestReward(questBase, rewardType, rewardValue));
      }
      questBase.buildGUI();
      index++;
    }
  }

}
