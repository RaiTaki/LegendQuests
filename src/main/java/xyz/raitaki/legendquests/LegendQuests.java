package xyz.raitaki.legendquests;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.legendquests.commands.EditQuestCommand;
import xyz.raitaki.legendquests.commands.completer.EditQuestCompleter;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.questhandlers.placeholders.CheckpointPlaceholder;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.PacketDisplay;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public final class LegendQuests extends JavaPlugin {

  private static LegendQuests instance;

  @Override
  public void onEnable() {
    instance = this;
    EconomyUtils.setupEconomy();
    registerCommands();

    new LanguageConfig("language.yml");
    new SettingsConfig("settings.yml");

    QuestBase questBase = new QuestBase("test", "test", 20 * 60 * 1000);
    questBase.addReward(new QuestReward(questBase, RewardTypeEnum.MONEY, "100"));
    questBase.addCheckPoint(
        new InteractionCheckpoint(questBase, CheckPointTypeEnum.INTERECT, "Zombie", "Zombie"));
    questBase.addCheckPoint(
        new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION, "KillZombie",
            "Zombie", "Accept", "Reject"));
    questBase.addCheckPoint(new KillCheckpoint(questBase, CheckPointTypeEnum.KILL, "Zombie", 2));
    questBase.addCheckPoint(new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION,
        "Kill zombie completed", "Zombie", "", ""));
    questBase.addCheckPoint(
        new InteractionCheckpoint(questBase, CheckPointTypeEnum.INTERECT, "Zombie2", "Skeleton"));
    questBase.addCheckPoint(new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION,
        "You are a real skeleton", "Zombie", "", ""));
    questBase.buildGUI();

    Bukkit.broadcastMessage(questBase.getAsJSON());
    QuestManager.registerEvents();
    CheckpointPlaceholder.registerPlaceholder();
    PacketDisplay.startUpdateTimer();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }

  public static LegendQuests getInstance() {
    return instance;
  }

  public void registerCommands(){
    getCommand("editquest").setExecutor(new EditQuestCommand());
    getCommand("editquest").setTabCompleter(new EditQuestCompleter());
  }
}
