package xyz.raitaki.legendquests;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.questhandlers.placeholders.CheckpointPlaceholder;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.LanguageConfig;

public final class LegendQuests extends JavaPlugin {

    private static LegendQuests instance;
    @Override
    public void onEnable() {
        instance = this;
        EconomyUtils.setupEconomy();
        QuestBase questBase = new QuestBase("test", "test");
        questBase.addReward(new QuestReward(questBase, QuestReward.RewardType.MONEY, "100"));
        questBase.addCheckPoint(new InteractionCheckpoint(questBase, QuestCheckpoint.CheckPointType.INTERECT, "Zombie", "Zombie"));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, QuestCheckpoint.CheckPointType.CONVERSATION, "KillZombie", "Zombie"));
        questBase.addCheckPoint(new KillCheckpoint(questBase, QuestCheckpoint.CheckPointType.KILL, "Zombie", 2));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, QuestCheckpoint.CheckPointType.CONVERSATION, "Kill zombie completed", "Zombie"));
        questBase.addCheckPoint(new InteractionCheckpoint(questBase, QuestCheckpoint.CheckPointType.INTERECT, "Zombie2", "Skeleton"));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, QuestCheckpoint.CheckPointType.CONVERSATION, "You are a real skeleton", "Zombie"));
        questBase.buildGUI();

        Bukkit.broadcastMessage(questBase.getAsJSON());
        QuestManager.registerEvents();
        CheckpointPlaceholder.registerPlaceholder();

        new LanguageConfig("language.yml");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LegendQuests getInstance() {
        return instance;
    }
}
