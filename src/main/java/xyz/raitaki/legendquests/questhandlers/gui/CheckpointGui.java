package xyz.raitaki.legendquests.questhandlers.gui;

import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum.ACCEPT_TEXT;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum.AMOUNT;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum.DECLINE_TEXT;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum.NPC_NAME;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum.VALUE;

import de.themoep.inventorygui.DynamicGuiElement;
import de.themoep.inventorygui.GuiElement.Action;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.TextTypeEnum;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.InteractionCheckpoint;
import xyz.raitaki.legendquests.questhandlers.checkpoints.KillCheckpoint;
import xyz.raitaki.legendquests.utils.TextUtils;

public class CheckpointGui {

  private QuestBase questBase;
  private QuestGui questGUI;
  private QuestCheckpoint checkpoint;
  private QuestCheckpoint newCheckpoint;

  private CheckPointTypeEnum newType;
  private String newValue = "";
  private String newNpcName = "";
  private int newAmount = 0;
  private InventoryGui checkpointGUI;
  private boolean saved = false;
  private EditTypeEnum editType;
  private TextTypeEnum textType;
  private String newAcceptText = "";
  private String newDeclineText = "";

  public CheckpointGui(QuestBase quest, QuestGui questGUI, QuestCheckpoint checkpoint) {
    this.questBase = quest;
    this.questGUI = questGUI;
    this.checkpoint = checkpoint;

    String[] guiSetup = {
        "         ",
        "  t   v  ",
        "    c    ",
        "  d   e  ",
        "a       b"
    };

    checkpointGUI = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);

    newType = checkpoint.getType();
    newValue = checkpoint.getValue();

    if (checkpoint instanceof KillCheckpoint kill) {
      newAmount = kill.getAmount();
    } else if (checkpoint instanceof InteractionCheckpoint interaction) {
      newNpcName = interaction.getNpcName();
    } else {
      ConversationCheckpoint conversation = (ConversationCheckpoint) checkpoint;
      newNpcName = conversation.getNpcName();
      newAcceptText = conversation.getAcceptText();
      newDeclineText = conversation.getDeclineText();
    }

    buildCheckpointGUI();
  }

  public void buildCheckpointGUI() {
    checkpointGUI.setFiller(questGUI.getFiller());

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

    addStaticElement(new ItemStack(Material.PAPER), 'v', click -> {
      setChangeType(click.getWhoClicked(), VALUE);
      return true;

    }, TextUtils.replaceColors("<SOLID:7d7d7d>Text: <SOLID:9ADB4F>" + newValue));

    if (newType == CheckPointTypeEnum.KILL) {
      addStaticElement(new ItemStack(Material.PAPER), 'c', click -> {
        setChangeType(click.getWhoClicked(), AMOUNT);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>Amount: <SOLID:9ADB4F>" + newValue));
    } else if (newType == CheckPointTypeEnum.INTERECT) {
      addStaticElement(new ItemStack(Material.PAPER), 'c', click -> {
        setChangeType(click.getWhoClicked(), NPC_NAME);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>NPC Name: <SOLID:9ADB4F>" + newNpcName));
    } else {
      addStaticElement(new ItemStack(Material.PAPER), 'c', click -> {
        setChangeType(click.getWhoClicked(), NPC_NAME);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>NPC Name: <SOLID:9ADB4F>" + newNpcName));

      addStaticElement(new ItemStack(Material.PAPER), 'd', click -> {
        setChangeType(click.getWhoClicked(), ACCEPT_TEXT);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>ACCEPT" + " Text: <SOLID:9ADB4F>" + newAcceptText));

      addStaticElement(new ItemStack(Material.PAPER), 'e', click -> {
        setChangeType(click.getWhoClicked(), DECLINE_TEXT);
        return true;

      }, TextUtils.replaceColors(
          "<SOLID:7d7d7d>DECLINE" + " Text: <SOLID:9ADB4F>" + newDeclineText));
    }

    addStaticElement(new ItemStack(Material.GREEN_WOOL), 'b', click -> {
      saved = true;

      if (newType == CheckPointTypeEnum.KILL) {
        newCheckpoint = new KillCheckpoint(questBase, newType, newValue, newAmount);
      } else if (newType == CheckPointTypeEnum.INTERECT) {
        newCheckpoint = new InteractionCheckpoint(questBase, newType, newValue, newNpcName);
      } else {
        newCheckpoint = new ConversationCheckpoint(questBase, newType, newValue, newNpcName,
            newAcceptText, newDeclineText);
      }
      checkpointGUI.close(click.getWhoClicked());

      int index = questBase.getCheckPoints().indexOf(checkpoint);
      questBase.getCheckPoints().set(index, newCheckpoint);
      questGUI.openCheckpointGUI(click.getWhoClicked());
      questGUI.setEditedCheckpoint(null);

      QuestUpdateEvent event = new QuestUpdateEvent(questBase);
      LegendQuests.getInstance().getServer().getPluginManager().callEvent(event);
      questGUI.setEditGuiType(null);
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Save"));

    checkpointGUI.setCloseAction(close -> {
      if (!saved) {
        sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Checkpoint not saved!"));
        questGUI.setEditGuiType(null);
      }
      questGUI.openCheckpointGUI(close.getPlayer());
      questGUI.setEditedCheckpoint(null);
      questGUI.setEditGuiType(null);

      return true;
    });
  }

  public void addStaticElement(ItemStack item, char slot, Action action, String... text) {
    checkpointGUI.addElement(new StaticGuiElement(slot, item, 1, action, text));
  }

  public void sendMessage(String message) {
    questGUI.getEditor().sendMessage(message);
  }

  public void setChatMessage(String message) {
    if (editType == null) {
      return;
    } else if (editType == VALUE) {
      newValue = message;
    } else if (editType == NPC_NAME) {
      newNpcName = message;
    } else if (editType == ACCEPT_TEXT) {
      newAcceptText = message;
    } else if (editType == DECLINE_TEXT) {
      newDeclineText = message;
    } else {
      newAmount = Integer.parseInt(message);
    }

    buildCheckpointGUI();
    checkpointGUI.draw();
    openCheckpointGUISync();

    editType = null;
  }

  public void setChangeType(HumanEntity clicker, EditTypeEnum editType) {
    this.editType = editType;
    checkpointGUI.close(clicker);
    sendMessage(
        TextUtils.replaceColors("<SOLID:00ff08>Enter new " + editType.getText() + " through chat"));
  }

  public void openCheckpointGUI() {
    checkpointGUI.show(questGUI.getEditor());
  }

  public void openCheckpointGUISync() {
    new BukkitRunnable() {
      @Override
      public void run() {
        checkpointGUI.show(questGUI.getEditor());
      }
    }.runTask(LegendQuests.getInstance());
  }

  public EditTypeEnum getEditType() {
    return editType;
  }

  public void setEditType(EditTypeEnum editType) {
    this.editType = editType;
  }

  private CheckPointTypeEnum nextType(CheckPointTypeEnum type) {
    return switch (type) {
      case CONVERSATION -> CheckPointTypeEnum.INTERECT;
      case INTERECT -> CheckPointTypeEnum.KILL;
      case KILL -> CheckPointTypeEnum.CONVERSATION;
    };
  }
}
