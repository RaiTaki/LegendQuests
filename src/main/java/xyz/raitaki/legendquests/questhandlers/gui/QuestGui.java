package xyz.raitaki.legendquests.questhandlers.gui;

import de.themoep.inventorygui.GuiElement.Action;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import java.util.LinkedList;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.utils.TextUtils;

public class QuestGui {

  private final ItemStack filler;
  private final QuestBase questBase;
  private InventoryGui mainGui;
  private InventoryGui checkpointGui;
  private InventoryGui rewardGui;
  private Player editor;

  private CheckpointGui editedCheckpoint;
  private RewardGui editedReward;
  private EditGuiTypeEnum editGuiType;

  public QuestGui(QuestBase questBase) {
    this.questBase = questBase;

    filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    filler.getItemMeta().setDisplayName("");

    buildMainGUI();
    buildRewardGUI();
    buildCheckpointGUI();
  }

  public void buildMainGUI() {
    String[] guiSetup = {
        "         ",
        "  a   b  ",
        "         ",
        "  s i z  ",
        "         "
    };
    mainGui = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);
    mainGui.setFiller(filler);

    //NAME & DESC
    addStaticElement(mainGui, new ItemStack(Material.PAPER), 'a', click -> true,
        TextUtils.replaceColors("<SOLID:7d7d7d>Quest: <SOLID:9ADB4F>" + questBase.getName()),
        TextUtils.replaceColors(
            "<SOLID:7d7d7d>Description: <SOLID:9ADB4F>" + questBase.getDescription())
    );

    //DETAILS

    addStaticElement(mainGui, new ItemStack(Material.PAPER), 'b', action -> true,
        TextUtils.replaceColors(
            "<SOLID:7d7d7d>Rewards: <SOLID:9ADB4F>" + questBase.getRewards().size()),
        TextUtils.replaceColors(
            "<SOLID:7d7d7d>Checkpoints: <SOLID:9ADB4F>" + questBase.getCheckPoints().size())
    );

    //SAVE
    addStaticElement(mainGui, new ItemStack(Material.PAPER), 'i', click -> {
      QuestManager.updatePlayersQuest();
      mainGui.close(click.getWhoClicked());
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Close"));

    //CHECKPOINTS
    addStaticElement(mainGui, new ItemStack(Material.PAPER), 'z', click -> {
      openCheckpointGUI(click.getWhoClicked());
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Checkpoints"));

    //REWARDS
    addStaticElement(mainGui, new ItemStack(Material.GOLD_INGOT), 's', click -> {
      openRewardGUI(click.getWhoClicked());
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Rewards"));

    mainGui.setCloseAction(close -> {
      editor = null;
      mainGui.close(close.getPlayer());
      QuestManager.updatePlayersQuest();
      return true;
    });
  }

  public void buildCheckpointGUI() {
    String[] guiSetup = {
        "ggggggggg",
        "ggggggggg",
        "ggggggggg",
        "ggggggggg",
        "a       b"
    };

    checkpointGui = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);
    checkpointGui.setFiller(filler);

    GuiElementGroup group = new GuiElementGroup('g');
    int i = 0;
    for (QuestCheckpoint checkPoint : questBase.getCheckPoints()) {
      int finalI = i;
      group.addElement(new StaticGuiElement('g', new ItemStack(Material.PAPER, i + 1), i + 1,
          click -> {
            if (click.getType() == ClickType.LEFT) {
              CheckpointGui checkpointGUI = new CheckpointGui(questBase, this,
                  checkPoint);
              checkpointGUI.openCheckpointGUI();
              setEditedCheckpoint(checkpointGUI);
              editGuiType = EditGuiTypeEnum.CHECKPOINT;
            } else if (click.getType() == ClickType.RIGHT) {
              moveBack(questBase.getCheckPoints(), finalI);
              updateCheckpointGUI();
            } else if (click.getType() == ClickType.SHIFT_RIGHT) {
              moveForward(questBase.getCheckPoints(), finalI);
              updateCheckpointGUI();
            } else if (click.getType() == ClickType.MIDDLE) {
              questBase.getCheckPoints().remove(finalI);
              updateCheckpointGUI();
            }
            return true;
          },
          TextUtils.replaceColors("<SOLID:7d7d7d>Checkpoint " + (i + 1)),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Type: <SOLID:9ADB4F>" + checkPoint.getType().toString()),
          TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + checkPoint.getValue()),
          TextUtils.replaceColors("<SOLID:7d7d7d>To edit, <SOLID:eaff00>LEFT CLICK"),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>To cycle back through list, <SOLID:eaff00>RIGHT CLICK"),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>To cycle forward through list, <SOLID:eaff00>SHIFT + RIGHT CLICK"),
          TextUtils.replaceColors("<SOLID:7d7d7d>To delete, <SOLID:eaff00>MIDDLE CLICK")
      ));
      i++;
    }
    checkpointGui.addElement(group);

    addStaticElement(checkpointGui, new ItemStack(Material.GREEN_WOOL), 'b', action -> {
      ConversationCheckpoint conversationCheckpoint = new ConversationCheckpoint(questBase,
          CheckPointTypeEnum.CONVERSATION, "NPC_TEXT", "NPC_NAME", "ACCEPT_TEXT",
          "DECLINE_TEXT");
      questBase.addCheckPoint(conversationCheckpoint);
      updateCheckpointGUI();
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Add Checkpoint"));

    addStaticElement(checkpointGui, new ItemStack(Material.ARROW), 'a', action -> {
      checkpointGui.close(action.getWhoClicked());
      openMainGUI(action.getWhoClicked());
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Back"));

    checkpointGui.setCloseAction(close -> {
      openMainGUI(close.getPlayer());
      return true;
    });
  }

  public void updateMainGUI() {
    mainGui.close(editor);
    buildMainGUI();
    mainGui.show(editor);
  }

  public void updateCheckpointGUI() {
    checkpointGui.close(editor);
    buildCheckpointGUI();
    checkpointGui.show(editor);
  }

  public void updateRewardGUI() {
    rewardGui.close(editor);
    buildRewardGUI();
    rewardGui.show(editor);
  }

  public void buildRewardGUI() {
    String[] guiSetup = {
        "ggggggggg",
        "ggggggggg",
        "ggggggggg",
        "ggggggggg",
        "a       b"
    };

    rewardGui = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);
    rewardGui.setFiller(new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1));

    GuiElementGroup group = new GuiElementGroup('g');
    int i = 0;
    for (QuestReward reward : questBase.getRewards()) {
      int finalI = i;

      group.addElement(new StaticGuiElement('g', new ItemStack(Material.PAPER), i + 1,
          click -> {
            if (click.getType() == ClickType.LEFT) {
              RewardGui rewardGUI = new RewardGui(questBase, this, reward);
              rewardGUI.openRewardGUI(click.getWhoClicked());
              editGuiType = EditGuiTypeEnum.REWARD;
              setEditedReward(rewardGUI);
            } else if (click.getType() == ClickType.RIGHT) {
              moveBack(questBase.getRewards(), finalI);
              updateRewardGUI();
            } else if (click.getType() == ClickType.SHIFT_RIGHT) {
              moveForward(questBase.getRewards(), finalI);
              updateRewardGUI();
            }
            return true;
          },
          TextUtils.replaceColors("<SOLID:7d7d7d>Reward " + (i + 1)),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Type: <SOLID:9ADB4F>" + reward.getType().toString()),
          TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + reward.getValue())
      ));
      i++;
    }
    rewardGui.addElement(group);

    addStaticElement(rewardGui, new ItemStack(Material.GOLD_INGOT), 'b', action -> {
      QuestReward questReward = new QuestReward(questBase, QuestReward.RewardTypeEnum.MONEY,
          "0");
      questBase.addReward(questReward);
      updateRewardGUI();
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Add Reward"));

    addStaticElement(rewardGui, new ItemStack(Material.ARROW), 'a', action -> {
      rewardGui.close(action.getWhoClicked());
      openMainGUI(action.getWhoClicked());
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Back"));

  }

  public void openMainGUI(Player player) {
    buildMainGUI();
    mainGui.show(player);
    editor = player;
  }

  public void openMainGUI(HumanEntity player) {
    buildMainGUI();
    mainGui.show(player);
    editor = (Player) player;
  }

  public void openCheckpointGUI(Player player) {
    buildCheckpointGUI();
    checkpointGui.show(player);
  }

  public void openCheckpointGUI(HumanEntity player) {
    buildCheckpointGUI();
    checkpointGui.show(player);
  }

  public void openRewardGUI(Player player) {
    buildRewardGUI();
    rewardGui.show(player);
  }

  public void openRewardGUI(HumanEntity player) {
    buildRewardGUI();
    rewardGui.show(player);
  }

  public void addStaticElement(InventoryGui gui, ItemStack item, char slot, Action action,
      String... text) {
    gui.addElement(new StaticGuiElement(slot, item, 1, action, text));
  }

  public QuestBase getQuestBase() {
    return questBase;
  }

  public InventoryGui getMainGui() {
    return mainGui;
  }

  public InventoryGui getCheckpointGui() {
    return checkpointGui;
  }

  public InventoryGui getRewardGui() {
    return rewardGui;
  }

  public Player getEditor() {
    return editor;
  }

  public void setEditor(Player editor) {
    this.editor = editor;
  }

  public RewardGui getEditedReward() {
    return editedReward;
  }

  public ItemStack getFiller() {
    return filler;
  }

  public void setEditedCheckpoint(CheckpointGui checkpoint) {
    editedCheckpoint = checkpoint;
  }

  public void setEditedReward(RewardGui reward) {
    editedReward = reward;
  }

  public EditGuiTypeEnum getEditGuiType() {
    return editGuiType;
  }

  public void setEditGuiType(EditGuiTypeEnum type) {
    editGuiType = type;
  }

  public CheckpointGui getEditedCheckpoint() {
    return editedCheckpoint;
  }

  private <T> void moveBack(LinkedList<T> list, int selectedIndex) {
    if (selectedIndex > 0 && selectedIndex < list.size()) {
      T temp = list.get(selectedIndex);
      list.set(selectedIndex, list.get(selectedIndex - 1));
      list.set(selectedIndex - 1, temp);
    }
  }

  public static <T> void moveForward(LinkedList<T> list, int selectedIndex) {
    if (selectedIndex >= 0 && selectedIndex < list.size() - 1) {
      T temp = list.get(selectedIndex);
      list.set(selectedIndex, list.get(selectedIndex + 1));
      list.set(selectedIndex + 1, temp);
    }
  }

  enum EditTypeEnum {
    VALUE("Value"),
    NPC_NAME("NPC Name"),
    AMOUNT("Amount"),
    ACCEPT_TEXT("Accept Text"),
    DECLINE_TEXT("Decline Text");

    String text;

    EditTypeEnum(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  public enum EditGuiTypeEnum {
    CHECKPOINT,
    REWARD
  }
}
