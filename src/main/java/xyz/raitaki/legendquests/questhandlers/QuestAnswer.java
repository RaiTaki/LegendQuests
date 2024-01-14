package xyz.raitaki.legendquests.questhandlers;

import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;

public class QuestAnswer {

  private QuestBase quest;
  private QuestCheckpoint checkPoint;
  private String answerText;
  private AnswerTypeEnum answerType;

  public QuestAnswer(QuestBase quest, QuestCheckpoint checkPoint, String answerText,
      AnswerTypeEnum answerType) {
    this.quest = quest;
    this.checkPoint = checkPoint;
    this.answerText = answerText;
    this.answerType = answerType;
  }

  public void answerQuestion(Player player) {
    switch (answerType) {
      case BACK:
        int checkpointIndex = quest.getCheckPoints().indexOf(checkPoint);
        QuestCheckpoint lastCheckPoint;
        if (checkpointIndex > 0) {
          lastCheckPoint = quest.getCheckPoints().get(checkpointIndex - 1);
        } else {
          lastCheckPoint = quest.getCheckPoints().get(0);
        }
        if (lastCheckPoint instanceof ConversationCheckpoint) {
          ((ConversationCheckpoint) lastCheckPoint).sendMessage(player);
        }

        break;
      case NEXT:
        break;
      case END:
        break;
    }
  }

  public enum AnswerTypeEnum {
    BACK,
    NEXT,
    END,
  }
}
