package xyz.raitaki.legendquests.questhandlers;

import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.utils.TextUtils;

public class QuestGUI {

    private ItemStack filler;
    private QuestBase questBase;
    private InventoryGui mainGUI;
    private InventoryGui checkpointGUI;
    private InventoryGui rewardGUI;

    public QuestGUI(QuestBase questBase) {
        this.questBase = questBase;

        filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        filler.getItemMeta().setDisplayName("");

        buildMainGUI();
        buildRewardGUI();
        buildCheckpointGUI();
    }

    public void buildMainGUI(){
        String[] guiSetup = {
                "         ",
                "  a   b  ",
                "         ",
                "  s i z  ",
                "         "
        };
        mainGUI = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(), guiSetup);
        mainGUI.setFiller(filler);
        mainGUI.addElement(new StaticGuiElement('a',
                new ItemStack(Material.PAPER),
                1,
                click -> {
                    return true;
                },
                TextUtils.replaceColors("<SOLID:7d7d7d>Quest: <SOLID:9ADB4F>" + questBase.getName()),
                        TextUtils.replaceColors("<SOLID:7d7d7d>Description: <SOLID:9ADB4F>" + questBase.getDescription())
        ));

        mainGUI.addElement(new StaticGuiElement('b',
                new ItemStack(Material.PAPER),
                1,
                click -> {
                    return true;
                },
                TextUtils.replaceColors("<SOLID:7d7d7d>Rewards: <SOLID:9ADB4F>" + questBase.getRewards().size()),
                        TextUtils.replaceColors("<SOLID:7d7d7d>Checkpoints: <SOLID:9ADB4F>" + questBase.getCheckPoints().size())
        ));

        mainGUI.addElement(new StaticGuiElement('i',
                new ItemStack(Material.GOLD_BLOCK),
                1,
                click -> {
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Save Quest")
        ));

        mainGUI.addElement(new StaticGuiElement('z',
                new ItemStack(Material.REDSTONE),
                1,
                click -> {
                    openCheckpointGUI(click.getWhoClicked());
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Checkpoints")
        ));

        mainGUI.addElement(new StaticGuiElement('s',
                new ItemStack(Material.GOLD_INGOT),
                1,
                click -> {
                    openRewardGUI(click.getWhoClicked());
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Rewards")
        ));
    }

    public void buildCheckpointGUI(){
        String[] guiSetup = {
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "a       b"
        };

        checkpointGUI = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(), guiSetup);
        checkpointGUI.setFiller(filler);

        GuiElementGroup group = new GuiElementGroup('g');
        int i = 0;
        for(QuestCheckpoint checkPoint : questBase.getCheckPoints()){
            group.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>Checkpoint " + i),
                    TextUtils.replaceColors("<SOLID:7d7d7d>Type: <SOLID:9ADB4F>" + checkPoint.getType().toString()),
                    TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + checkPoint.getValue())
            ));
            i++;
        }
        checkpointGUI.addElement(group);

        checkpointGUI.addElement(new StaticGuiElement('b',
                new ItemStack(Material.GREEN_WOOL),
                1,
                click -> {
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Add Checkpoint")
        ));

        checkpointGUI.addElement(new StaticGuiElement('a',
                new ItemStack(Material.ARROW),
                1,
                click -> {
                    checkpointGUI.close(click.getWhoClicked());
                    openMainGUI(click.getWhoClicked());
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Back")
        ));
    }

    public void buildRewardGUI(){
        String[] guiSetup = {
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "ggggggggg",
                "a       b"
        };

        rewardGUI = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(), guiSetup);
        rewardGUI.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));

        GuiElementGroup group = new GuiElementGroup('g');
        int i = 0;
        for(QuestReward reward : questBase.getRewards()){
            group.addElement(new StaticGuiElement('g',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>Reward " + i),
                    TextUtils.replaceColors("<SOLID:7d7d7d>Type: <SOLID:9ADB4F>" + reward.getType().toString()),
                    TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + reward.getValue())
            ));
            i++;
        }

        rewardGUI.addElement(group);

        rewardGUI.addElement(new StaticGuiElement('b',
                new ItemStack(Material.GREEN_WOOL),
                1,
                click -> {
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Add Reward")
        ));

        rewardGUI.addElement(new StaticGuiElement('a',
                new ItemStack(Material.ARROW),
                1,
                click -> {
                    rewardGUI.close(click.getWhoClicked());
                    openMainGUI(click.getWhoClicked());
                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Back")
        ));
    }

    public void openMainGUI(Player player){
        buildMainGUI();
        mainGUI.show(player);
    }

    public void openMainGUI(HumanEntity player){
        buildMainGUI();
        mainGUI.show(player);
    }

    public void openCheckpointGUI(Player player){
        buildCheckpointGUI();
        checkpointGUI.show(player);
    }

    public void openCheckpointGUI(HumanEntity player){
        buildCheckpointGUI();
        checkpointGUI.show(player);
    }

    public void openRewardGUI(Player player){
        buildRewardGUI();
        rewardGUI.show(player);
    }

    public void openRewardGUI(HumanEntity player){
        buildRewardGUI();
        rewardGUI.show(player);
    }

    public QuestBase getQuestBase() {
        return questBase;
    }

    public InventoryGui getMainGUI() {
        return mainGUI;
    }

    public InventoryGui getCheckpointGUI() {
        return checkpointGUI;
    }

    public InventoryGui getRewardGUI() {
        return rewardGUI;
    }
}
