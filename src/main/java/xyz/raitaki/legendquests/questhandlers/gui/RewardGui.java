package xyz.raitaki.legendquests.questhandlers.gui;

import static xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum.ITEM;
import static xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum.MONEY;
import static xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum.XP;
import static xyz.raitaki.legendquests.questhandlers.gui.QuestGui.EditTypeEnum;

import de.themoep.inventorygui.GuiElement.Action;
import de.themoep.inventorygui.InventoryGui;
import de.themoep.inventorygui.StaticGuiElement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.events.QuestUpdateEvent;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestReward;
import xyz.raitaki.legendquests.questhandlers.QuestReward.RewardTypeEnum;
import xyz.raitaki.legendquests.utils.ItemUtils;
import xyz.raitaki.legendquests.utils.TextUtils;

public class RewardGui {

  private QuestBase questBase;
  private QuestGui questGui;
  private QuestReward reward;
  private QuestReward newReward;

  private RewardTypeEnum newType;
  private String newValue = "";
  private InventoryGui rewardGui;
  private boolean saved = false;
  private EditTypeEnum editTypeEnum;
  String[] guiSetup;

  public RewardGui(QuestBase quest, QuestGui questGui, QuestReward reward) {
    this.questBase = quest;
    this.questGui = questGui;
    this.reward = reward;

    guiSetup = new String[]{
        "         ",
        "  t   v  ",
        "    c    ",
        "a       b"
    };

    rewardGui = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);

    newType = reward.getType();
    newValue = reward.getValue();

    rewardGui.setFiller(this.questGui.getFiller());

    buildRewardGUI();
  }

  /**
   * builds the reward gui based on the selected reward
   */
  public void buildRewardGUI() {
    rewardGui = new InventoryGui(LegendQuests.getInstance(), "Quest: " + questBase.getName(),
        guiSetup);
    rewardGui.setFiller(questGui.getFiller());

    addStaticElement(new ItemStack(Material.PAPER), 't', click -> {
          newType = nextType(newType);
          updateRewardGui();
          return true;

        }, TextUtils.replaceColors("<SOLID:7d7d7d>Type: <SOLID:00ff08>" + newType),
        TextUtils.replaceColors("<SOLID:7d7d7d>To change type, <SOLID:eaff00>LEFT CLICK"));

    if (newType == ITEM) {
      ItemStack itemStack = null;
      try {
        itemStack = ItemUtils.stringToItem(newValue);
      } catch (Exception e) {
        Bukkit.getLogger().warning("Invalid item: " + newValue);
        sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Invalid item!"));
      }

      if (itemStack == null) {
        itemStack = new ItemStack(Material.BARRIER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(TextUtils.replaceColors("<SOLID:ff0000>Invalid item!"));
        itemStack.setItemMeta(itemMeta);
      }

      addStaticElement(itemStack, 'c', click -> {
        if (click.getCursor() == null) {
          sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Invalid item! Hold Item on cursor!"));
          return false;
        }
        newValue = ItemUtils.itemToString(click.getCursor());
        updateRewardGui();
        return true;
      }, TextUtils.replaceColors(
          "<SOLID:7d7d7d>Item: <SOLID:9ADB4F>" + itemStack.getItemMeta().getDisplayName()));

    } else {
      addStaticElement(new ItemStack(Material.PAPER), 'c', click -> {
        setChangeType(click.getWhoClicked(), EditTypeEnum.VALUE);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + newValue));

      addStaticElement(new ItemStack(Material.PAPER), 'c', click -> {
        setChangeType(click.getWhoClicked(), EditTypeEnum.VALUE);
        return true;

      }, TextUtils.replaceColors("<SOLID:7d7d7d>Value: <SOLID:9ADB4F>" + newValue));
    }

    addStaticElement(new ItemStack(Material.GREEN_WOOL), 'b', click -> {
      saved = true;
      int index = questBase.getRewards().indexOf(reward);
      QuestReward newReward = new QuestReward(questBase, newType, newValue);

      questBase.getRewards().set(index, newReward);
      questGui.openRewardGUI(click.getWhoClicked());
      questGui.setEditedReward(null);

      QuestUpdateEvent event = new QuestUpdateEvent(questBase);
      LegendQuests.getInstance().getServer().getPluginManager().callEvent(event);

      rewardGui.close(click.getWhoClicked());
      questGui.setEditGuiType(null);
      return true;
    }, TextUtils.replaceColors("<SOLID:00ff08>Save"));

    rewardGui.setCloseAction(close -> {
      if (!saved) {
        sendMessage(TextUtils.replaceColors("<SOLID:ff0000>Reward not saved!"));
        questGui.setEditGuiType(null);
      }
      questGui.openRewardGUI(close.getPlayer());
      questGui.setEditedReward(null);
      questGui.setEditGuiType(null);
      return true;
    });
  }

  /**
   * send message to the player
   *
   * @param message the message to send
   */
  public void sendMessage(String message) {
    questGui.getEditor().sendMessage(message);
  }

  /**
   * add static elemnt to the gui
   *
   * @param item   the item of the element
   * @param slot   the slot of the element
   * @param action the action of the element
   * @param text   the text of the element
   */
  public void addStaticElement(ItemStack item, char slot, Action action, String... text) {
    rewardGui.addElement(new StaticGuiElement(slot, item, 1, action, text));
  }

  /**
   * open the reward gui to the player
   *
   * @param player player to open the gui
   */
  public void openRewardGUI(Player player) {
    rewardGui.show(player);
  }

  /**
   * open the reward gui to the player
   *
   * @param player player to open the gui
   */
  public void openRewardGUI(HumanEntity player) {
    rewardGui.show(player);
  }

  /**
   * change the edit type of the reward
   *
   * @param clicker  the player who clicked
   * @param editType the edit type to change to
   */
  public void setChangeType(HumanEntity clicker, EditTypeEnum editType) {
    this.editTypeEnum = editType;
    rewardGui.close(clicker);
    sendMessage(
        TextUtils.replaceColors("<SOLID:00ff08>Enter new " + editType.getText() + " through chat"));
  }

  /**
   * get the next reward type
   *
   * @param type the current type
   * @return the next type
   */
  private RewardTypeEnum nextType(RewardTypeEnum type) {
    return switch (type) {
      case MONEY -> XP;
      case XP -> ITEM;
      case ITEM -> MONEY;
    };
  }

  /**
   * set the values based on the chat message
   *
   * @param message the message to set
   */
  public void setChatMessage(String message) {
    if (editTypeEnum == null) {
      return;
    }
    if (editTypeEnum == EditTypeEnum.VALUE) {
      newValue = message;
    }
    updateRewardGuiSync();

    editTypeEnum = null;
  }

  /**
   * update the reward gui
   */
  public void updateRewardGui() {
    rewardGui.close(questGui.getEditor());
    buildRewardGUI();
    rewardGui.draw();
    openRewardGUI(questGui.getEditor());
  }

  /**
   * update the reward gui synchronized
   */
  public void updateRewardGuiSync() {
    new BukkitRunnable() {
      @Override
      public void run() {
        updateRewardGui();
      }
    }.runTask(LegendQuests.getInstance());
  }
}
