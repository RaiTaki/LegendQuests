package xyz.raitaki.legendquests.questhandlers.placeholders;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.PlayerQuest;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.checkpoints.PlayerKillCheckpoint;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;

public class CheckpointPlaceholder extends PlaceholderExpansion {

  @Override
  public @NotNull String getIdentifier() {
    return "legendquest";
  }

  @Override
  public @NotNull String getAuthor() {
    return "RaiTaki";
  }

  @Override
  public @NotNull String getVersion() {
    return "0.1";
  }

  @Override
  public boolean canRegister() {
    return true;
  }

  @Override
  public boolean persist() {
    return true;
  }

  @Override
  public String onPlaceholderRequest(Player p, @NotNull String params) {
    String st = getStringByParam(params);
    if (p == null) {
      return st;
    }

    QuestPlayer player = QuestManager.getQuestPlayerByPlayer(p);
    if (player.getQuests().isEmpty()) {
      return st;
    }

    PlayerQuest quest = player.getQuestOnGoing();
    if (quest == null) {
      return st;
    }

    if (params.equalsIgnoreCase("checkpoint")) {
      if (quest.getCheckpoint() != null) {
        CheckPointTypeEnum type = quest.getCheckpoint().getType();
        if (type == CheckPointTypeEnum.KILL) {
          PlayerKillCheckpoint checkpoint = (PlayerKillCheckpoint) quest.getCheckpoint();
          String entityName = checkpoint.getValue();
          String counter = String.valueOf(checkpoint.getCounter());
          String amount = String.valueOf(checkpoint.getAmount());
          st = LanguageConfig.getInstance().get("placeholders.kill", true,
              "{entityName}", entityName,
              "{counter}", counter,
              "{max}", amount);
        }
        if (type == CheckPointTypeEnum.INTERACT) {
          st = LanguageConfig.getInstance()
              .get("placeholders.interact", true, "{entityName}", quest.getCheckpoint().getValue());
        }
      }
    }
    if (params.equalsIgnoreCase("questname")) {
      st = quest.getQuestName();
    }
    if (params.equalsIgnoreCase("questdescription")) {
      st = quest.getDescription();
    }
    if (params.equalsIgnoreCase("remainingtime")) {
      st = quest.getRemainingTimeFormatted();
    }
    return st;
  }

  public static void registerPlaceholder() {
    new CheckpointPlaceholder().register();
  }

  public String getStringByParam(String param) {
    if (param.equalsIgnoreCase("checkpoint")) {
      return LanguageConfig.getInstance().get("placeholders.noquest", true);
    } else {
      return "";
    }
  }
}