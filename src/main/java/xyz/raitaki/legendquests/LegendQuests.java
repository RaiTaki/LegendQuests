package xyz.raitaki.legendquests;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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
import xyz.raitaki.legendquests.utils.LanguageConfig;

public final class LegendQuests extends JavaPlugin {

    private static LegendQuests instance;
    @Override
    public void onEnable() {
        instance = this;
        EconomyUtils.setupEconomy();
        QuestBase questBase = new QuestBase("test", "test");
        questBase.addReward(new QuestReward(questBase, RewardTypeEnum.MONEY, "100"));
        questBase.addCheckPoint(new InteractionCheckpoint(questBase, CheckPointTypeEnum.INTERECT, "Zombie", "Zombie"));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION, "KillZombie", "Zombie"));
        questBase.addCheckPoint(new KillCheckpoint(questBase, CheckPointTypeEnum.KILL, "Zombie", 2));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION, "Kill zombie completed", "Zombie"));
        questBase.addCheckPoint(new InteractionCheckpoint(questBase, CheckPointTypeEnum.INTERECT, "Zombie2", "Skeleton"));
        questBase.addCheckPoint(new ConversationCheckpoint(questBase, CheckPointTypeEnum.CONVERSATION, "You are a real skeleton", "Zombie"));
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
