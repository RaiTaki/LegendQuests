package xyz.raitaki.legendquests;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.utils.EconomyUtils;

public final class LegendQuests extends JavaPlugin {

    private static LegendQuests instance;
    @Override
    public void onEnable() {
        instance = this;
        EconomyUtils.setupEconomy();
        QuestBase questBase = new QuestBase("test", "test");
        questBase.addCheckPoint(new QuestCheckpoint(questBase, QuestCheckpoint.CheckPointType.START, "test"));
        questBase.addCheckPoint(new QuestCheckpoint(questBase, QuestCheckpoint.CheckPointType.INTERECT, "test"));
        questBase.addCheckPoint(new QuestCheckpoint(questBase, QuestCheckpoint.CheckPointType.KILL, "test"));
        questBase.addCheckPoint(new QuestCheckpoint(questBase, QuestCheckpoint.CheckPointType.END, "test"));
        questBase.addReward(new QuestReward(questBase, QuestReward.RewardType.MONEY, "100"));

        System.out.println(questBase.getAsJSON());


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static LegendQuests getInstance() {
        return instance;
    }
}
