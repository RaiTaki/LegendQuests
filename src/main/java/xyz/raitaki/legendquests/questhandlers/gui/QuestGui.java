package xyz.raitaki.legendquests.questhandlers.gui;

import de.themoep.inventorygui.GuiElement.Action;
import de.themoep.inventorygui.GuiElementGroup;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import java.util.LinkedList;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint;
import xyz.raitaki.legendquests.questhandlers.QuestCheckpoint.CheckPointTypeEnum;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.checkpoints.ConversationCheckpoint;
import xyz.raitaki.legendquests.utils.PacketDisplay;
import xyz.raitaki.legendquests.utils.TextUtils;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

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
  private EditTypeEnum editType;

  public QuestGui(QuestBase questBase) {
    this.questBase = questBase;

    filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
    filler.getItemMeta().setDisplayName("");
  }

  /**
   * Build the main gui
   */
  public void buildMainGUI() {
    String[] guiSetup = {
        "    g    ",
        "         ",
        "  a d b  ",
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
        TextUtils.replaceColors("<SOLID:7d7d7d>Quest ID: <SOLID:9ADB4F>" + questBase.getQuestId()),
        TextUtils.replaceColors(
            "<SOLID:7d7d7d>Description: <SOLID:9ADB4F>" + questBase.getDescription()),
        TextUtils.replaceColors("<SOLID:7d7d7d>Time: <SOLID:9ADB4F>" + questBase.getTime()),
        TextUtils.replaceColors(
            "<SOLID:7d7d7d>Next Quest: <SOLID:9ADB4F>" + questBase.getNextQuestName())
    );

    addStaticElement(mainGui, new ItemStack(Material.PAPER), 'd', click -> {
      if(click.getType() == ClickType.LEFT) {
        setChangeType(click.getWhoClicked(), EditTypeEnum.DESCRIPTION);
        editGuiType = EditGuiTypeEnum.QUEST;
      } else if(click.getType() == ClickType.SHIFT_LEFT) {
        setChangeType(click.getWhoClicked(), EditTypeEnum.QUEST_NAME);
        editGuiType = EditGuiTypeEnum.QUEST;
      } else if(click.getType() == ClickType.RIGHT) {
        setChangeType(click.getWhoClicked(), EditTypeEnum.TIME);
        editGuiType = EditGuiTypeEnum.QUEST;
      } else if(click.getType() == ClickType.SHIFT_RIGHT) {
        setChangeType(click.getWhoClicked(), EditTypeEnum.NEXT_QUEST);
        editGuiType = EditGuiTypeEnum.QUEST;
      }
      return true;
    }, TextUtils.replaceColors("<SOLID:7d7d7d>Left click to edit description: <SOLID:9ADB4F>" + questBase.getDescription()),
        TextUtils.replaceColors("<SOLID:7d7d7d>Shift + Left click to edit quest name: <SOLID:9ADB4F>" + questBase.getName()),
        TextUtils.replaceColors("<SOLID:7d7d7d>Right click to edit time: <SOLID:9ADB4F>" + questBase.getTime()),
        TextUtils.replaceColors("<SOLID:7d7d7d>Shift + Right click to edit next quest: <SOLID:9ADB4F>" + questBase.getNextQuestName()));

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

      QuestUpdateEvent event = new QuestUpdateEvent(questBase);
      LegendQuests.getInstance().getServer().getPluginManager().callEvent(event);
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

    //TRACKER
    Location location = SettingsConfig.getInstance().getLocationValue("questtracker.location");
    if (location != null) {
      addStaticElement(mainGui, new ItemStack(Material.COMPASS), 'g', click -> {

            if (click.getType() == ClickType.LEFT) {
              setChangeType(click.getWhoClicked(), EditTypeEnum.X_VALUE);
              editGuiType = EditGuiTypeEnum.TRACKER;
            } else if (click.getType() == ClickType.RIGHT) {
              setChangeType(click.getWhoClicked(), EditTypeEnum.Y_VALUE);
              editGuiType = EditGuiTypeEnum.TRACKER;
            } else if (click.getType() == ClickType.SHIFT_LEFT) {
              setChangeType(click.getWhoClicked(), EditTypeEnum.Z_VALUE);
              editGuiType = EditGuiTypeEnum.TRACKER;
            } else if (click.getType() == ClickType.SHIFT_RIGHT) {
              setChangeType(click.getWhoClicked(), EditTypeEnum.YAW_VALUE);
              editGuiType = EditGuiTypeEnum.TRACKER;
            } else if (click.getType() == ClickType.MIDDLE) {
              setChangeType(click.getWhoClicked(), EditTypeEnum.PITCH_VALUE);
              editGuiType = EditGuiTypeEnum.TRACKER;
            }
            return true;
          }, TextUtils.replaceColors("<SOLID:00ff08>Tracker"),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Left click to set tracker X pos: <SOLID:00ff08>" + formatDouble(
                  location.getX())),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Right click to set tracker Y pos: <SOLID:00ff08>" + formatDouble(
                  location.getY())),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Shift + Left click to set tracker Z pos: <SOLID:00ff08>"
                  + formatDouble(location.getZ())),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Shift + Right click to set tracker Yaw: <SOLID:00ff08>" + formatDouble(
                  location.getYaw())),
          TextUtils.replaceColors(
              "<SOLID:7d7d7d>Middle click to set tracker Pitch: <SOLID:00ff08>" + formatDouble(
                  location.getPitch())));
    }

    mainGui.setCloseAction(close -> {
      editor = null;
      mainGui.close(close.getPlayer());
      QuestManager.updatePlayersQuest();

      QuestUpdateEvent event = new QuestUpdateEvent(questBase);
      LegendQuests.getInstance().getServer().getPluginManager().callEvent(event);
      return true;
    });
  }

  /**
   * Build the checkpoint gui
   */
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
      questBase.addCheckpoint(conversationCheckpoint);
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

  /**
   * Set the type of the change
   *
   * @param clicker  who clicked
   * @param editType type of the change
   */
  public void setChangeType(HumanEntity clicker, EditTypeEnum editType) {
    this.editType = editType;
    mainGui.close(clicker);
    sendMessage(
        TextUtils.replaceColors("<SOLID:00ff08>Enter new " + editType.getText() + " through chat"));
    if(editType == EditTypeEnum.TIME){
      sendMessage(TextUtils.replaceColors("<SOLID:00ff08>Format: <SOLID:9ADB4F>1D, 1H, 1M, 1S. Case sensitive!"));
    }
  }

  /**
   * Format double to 2 decimal places
   *
   * @param value the value to format
   * @return the formatted value
   */
  public String formatDouble(double value) {
    return String.format("%.2f", value);
  }

  /**
   * Do the change based on the string
   *
   * @param text the text to change to
   */
  public void doTrackerText(String text) {
    double value = 0;
    try {
      value = Double.parseDouble(text);
    } catch (NumberFormatException e) {
      sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Invalid number!"));
      return;
    }
    Location loc = SettingsConfig.getInstance().getLocationValue("questtracker.location");
    if (editType == EditTypeEnum.X_VALUE) {
      loc.setX(value);
    } else if (editType == EditTypeEnum.Y_VALUE) {
      loc.setY(value);
    } else if (editType == EditTypeEnum.Z_VALUE) {
      loc.setZ(value);
    } else if (editType == EditTypeEnum.YAW_VALUE) {
      loc.setYaw((float) value);
    } else if (editType == EditTypeEnum.PITCH_VALUE) {
      loc.setPitch((float) value);
    }

    editType = null;
    editGuiType = null;
    SettingsConfig.getInstance().setLocation("questtracker.location", loc);
    PacketDisplay.updateDisplayLocation();
    openMainGuiSynced(editor);
  }

  /**
   * Do the quest change based on the string
   *
   * @param text the text to change to
   */
  public void doQuestChange(String text) {
    if (editType == EditTypeEnum.DESCRIPTION) {
      questBase.setQuestDescription(text);
    } else if (editType == EditTypeEnum.QUEST_NAME) {
      questBase.setQuestName(text);
    } else if (editType == EditTypeEnum.TIME) {
      questBase.setTime(TextUtils.parseStringToTime(text));
    } else if (editType == EditTypeEnum.NEXT_QUEST) {
      questBase.setNextQuestName(text);
    }

    editType = null;
    editGuiType = null;
    openMainGuiSynced(editor);
  }

  /**
   * Build the reward gui
   */
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

  /**
   * open the main gui
   *
   * @param player the player to open the gui to
   */
  public void openMainGUI(Player player) {
    buildMainGUI();
    mainGui.show(player);
    editor = player;
  }

  /**
   * Send message to the player
   *
   * @param message the message to send
   */
  public void sendMessage(String message) {
    editor.sendMessage(message);
  }

  /**
   * Update the checkpoint gui
   */
  public void updateCheckpointGUI() {
    checkpointGui.close(editor);
    buildCheckpointGUI();
    checkpointGui.show(editor);
  }

  /**
   * Update the reward gui
   */
  public void updateRewardGUI() {
    rewardGui.close(editor);
    buildRewardGUI();
    rewardGui.show(editor);
  }

  /**
   * Open the main gui synced
   *
   * @param player the player to open the gui to
   */
  public void openMainGuiSynced(HumanEntity player) {
    new BukkitRunnable() {
      @Override
      public void run() {
        buildMainGUI();
        mainGui.show(player);
        editor = (Player) player;
      }
    }.runTask(LegendQuests.getInstance());
  }

  /**
   * Open the main gui synced
   *
   * @param player the player to open the gui to
   */
  public void openMainGUI(HumanEntity player) {
    buildMainGUI();
    mainGui.show(player);
    editor = (Player) player;
  }

  /**
   * Open the checkpoint gui
   *
   * @param player the player to open the gui to
   */
  public void openCheckpointGUI(HumanEntity player) {
    buildCheckpointGUI();
    checkpointGui.show(player);
  }

  /**
   * Open the reward gui
   *
   * @param player the player to open the gui to
   */
  public void openRewardGUI(HumanEntity player) {
    buildRewardGUI();
    rewardGui.show(player);
  }

  /**
   * Add static element to the gui
   *
   * @param gui    the gui to add the element to
   * @param item   the item to add
   * @param slot   the slot to add the item to
   * @param action the action to do when clicked
   * @param text   the text to add to the item
   */
  public void addStaticElement(InventoryGui gui, ItemStack item, char slot, Action action,
      String... text) {
    gui.addElement(new StaticGuiElement(slot, item, 1, action, text));
  }

  /**
   * @return Player who is editing the quest
   */
  public Player getEditor() {
    return editor;
  }

  /**
   * @return RewardGui that is being edited
   */
  public RewardGui getEditedReward() {
    return editedReward;
  }

  /**
   * @return ItemStack filler
   */
  public ItemStack getFiller() {
    return filler;
  }

  /**
   * set the edited checkpoint
   *
   * @param checkpoint the checkpoint to set
   */
  public void setEditedCheckpoint(CheckpointGui checkpoint) {
    editedCheckpoint = checkpoint;
  }

  /**
   * set the edited reward
   *
   * @param reward the reward to set
   */
  public void setEditedReward(RewardGui reward) {
    editedReward = reward;
  }

  /**
   * @return EditTypeEnum of the edit
   */
  public EditGuiTypeEnum getEditGuiType() {
    return editGuiType;
  }

  /**
   * set the edit type
   *
   * @param type the type to set
   */
  public void setEditGuiType(EditGuiTypeEnum type) {
    editGuiType = type;
  }

  /**
   * @return CheckpointGui that is being edited
   */
  public CheckpointGui getEditedCheckpoint() {
    return editedCheckpoint;
  }

  /**
   * Move the item back in the list
   *
   * @param list          the list to move the item in
   * @param selectedIndex the index of the item to move
   * @param <T>           the type of the list
   */
  private <T> void moveBack(LinkedList<T> list, int selectedIndex) {
    if (selectedIndex > 0 && selectedIndex < list.size()) {
      T temp = list.get(selectedIndex);
      list.set(selectedIndex, list.get(selectedIndex - 1));
      list.set(selectedIndex - 1, temp);
    }
  }

  /**
   * Move the item forward in the list
   *
   * @param list          the list to move the item in
   * @param selectedIndex the index of the item to move
   * @param <T>           the type of the list
   */
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
    DECLINE_TEXT("Decline Text"),
    X_VALUE("X"),
    Y_VALUE("Y"),
    Z_VALUE("Z"),
    YAW_VALUE("Yaw"),
    PITCH_VALUE("Pitch"),
    DESCRIPTION("Description"),
    QUEST_NAME("Quest Name"),
    TIME("Time"),
    NEXT_QUEST("Next Quest");

    String text;

    EditTypeEnum(String text) {
      this.text = text;
    }

    public String getText() {
      return text;
    }
  }

  public enum EditGuiTypeEnum {
    QUEST,
    CHECKPOINT,
    REWARD,
    TRACKER
  }
}
