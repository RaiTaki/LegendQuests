package xyz.raitaki.legendquests.questhandlers.gui;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.questhandlers.placeholders.CheckpointPlaceholder;
import xyz.raitaki.legendquests.utils.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckpointGUI {

    private QuestBase questBase;
    private QuestGUI questGUI;
    private QuestCheckpoint checkpoint;
    private QuestCheckpoint newCheckpoint;

    private QuestCheckpoint.CheckPointType newType;
    private String newValue = "";
    private String newNpcName = "";
    private int newAmount = 0;
    private InventoryGui checkpointGUI;
    private boolean saved = false;
    private EditType editType;

    public CheckpointGUI(QuestBase quest, QuestGUI questGUI, QuestCheckpoint checkpoint) {
        this.questBase = quest;
        this.questGUI = questGUI;
        this.checkpoint = checkpoint;

        String[] guiSetup = {
                "         ",
                "  t   v  ",
                "    c    ",
                "a       b"
        };

        checkpointGUI = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(), guiSetup);

        newType = checkpoint.getType();
        newValue = checkpoint.getValue();

        if(checkpoint instanceof KillCheckpoint kill){
            newAmount = kill.getAmount();
        } else if (checkpoint instanceof InteractionCheckpoint interaction) {
            newNpcName = interaction.getNpcName();
        }
        else {
            ConversationCheckpoint conversation = (ConversationCheckpoint) checkpoint;
            newNpcName = conversation.getNpcName();
        }

        buildCheckpointGUI();
    }

    public void buildCheckpointGUI(){
        checkpointGUI.setFiller(questGUI.getfiller());

        checkpointGUI.addElement(new DynamicGuiElement('t', (viewer) -> {
            return new StaticGuiElement('t',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        newType = nextType(newType);
                        buildCheckpointGUI();
                        checkpointGUI.draw();
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>Type: <SOLID:00ff08>" + newType),
                    TextUtils.replaceColors("<SOLID:7d7d7d>To change type, <SOLID:eaff00>LEFT CLICK")
            );
        }));

        checkpointGUI.addElement(new StaticGuiElement('v',
                new ItemStack(Material.PAPER),
                1,
                click -> {
                    editType = EditType.VALUE;
                    checkpointGUI.close(click.getWhoClicked());
                    questGUI.getEditor().sendMessage(TextUtils.replaceColors("<SOLID:00ff08>Enter new value through chat"));
                    return true;
                },
                TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + newValue)
        ));

        if(newType == QuestCheckpoint.CheckPointType.KILL){
            checkpointGUI.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        editType = EditType.AMOUNT;
                        checkpointGUI.close(click.getWhoClicked());
                        questGUI.getEditor().sendMessage(TextUtils.replaceColors("<SOLID:00ff08>Enter new amount through chat"));
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>Amount: <SOLID:9ADB4F>" + newAmount)
            ));
        } else if (newType == QuestCheckpoint.CheckPointType.INTERECT) {
            checkpointGUI.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        editType = EditType.NPC_NAME;
                        checkpointGUI.close(click.getWhoClicked());
                        questGUI.getEditor().sendMessage(TextUtils.replaceColors("<SOLID:00ff08>Enter new NPC name through chat"));
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>NPC Name: <SOLID:9ADB4F>" + newNpcName)
            ));
        }
        else {
            checkpointGUI.addElement(new StaticGuiElement('c',
                    new ItemStack(Material.PAPER),
                    1,
                    click -> {
                        return true;
                    },
                    TextUtils.replaceColors("<SOLID:7d7d7d>NPC Name: <SOLID:9ADB4F>" + newNpcName)
            ));
        }

        checkpointGUI.addElement(new StaticGuiElement('b',
                new ItemStack(Material.GREEN_WOOL),
                1,
                click -> {
                    saved = true;

                    if(newType == QuestCheckpoint.CheckPointType.KILL){
                        newCheckpoint = new KillCheckpoint(questBase, newType, newValue, newAmount);
                    } else if (newType == QuestCheckpoint.CheckPointType.INTERECT) {
                        newCheckpoint = new InteractionCheckpoint(questBase, newType, newValue, newNpcName);
                    }
                    else {
                        newCheckpoint = new ConversationCheckpoint(questBase, newType, newValue, newNpcName);
                    }
                    checkpointGUI.close(click.getWhoClicked());

                    int index = questBase.getCheckPoints().indexOf(checkpoint);
                    questBase.getCheckPoints().set(index, newCheckpoint);
                    questGUI.openCheckpointGUI(click.getWhoClicked());
                    questGUI.setEditedCheckpoint(null);

                    QuestUpdateEvent event = new QuestUpdateEvent(questBase);
                    LegendQuests.getInstance().getServer().getPluginManager().callEvent(event);

                    return true;
                },
                TextUtils.replaceColors("<SOLID:00ff08>Save")
        ));

        checkpointGUI.setCloseAction(close -> {
            if(!saved){
                close.getPlayer().sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Checkpoint not saved!"));
            }
            questGUI.openCheckpointGUI(close.getPlayer());
            questGUI.setEditedCheckpoint(null);
            return true;
        });
    }

    public void setChatMessage(String message){
        if(editType == null) return;
        if(editType == EditType.VALUE){
            newValue = message;
        }
        else if(editType == EditType.NPC_NAME){
            newNpcName = message;
        }
        else {
            newAmount = Integer.parseInt(message);
        }
        buildCheckpointGUI();
        checkpointGUI.draw();
        openCheckpointGUISync();

        editType = null;
    }

    public void openCheckpointGUI(){
        checkpointGUI.show(questGUI.getEditor());
    }

    public void openCheckpointGUISync(){
        new BukkitRunnable() {
            @Override
            public void run() {
                checkpointGUI.show(questGUI.getEditor());
            }
        }.runTask(LegendQuests.getInstance());
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
    }

    private QuestCheckpoint.CheckPointType nextType(QuestCheckpoint.CheckPointType type){
        return switch (type) {
            case CONVERSATION -> QuestCheckpoint.CheckPointType.INTERECT;
            case INTERECT -> QuestCheckpoint.CheckPointType.KILL;
            case KILL -> QuestCheckpoint.CheckPointType.CONVERSATION;
        };
    }

    enum EditType{
        VALUE,
        NPC_NAME,
        AMOUNT
    }
}
