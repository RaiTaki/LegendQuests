package xyz.raitaki.legendquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.utils.TextUtils;

public class EditQuestCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(!(sender instanceof Player player)) return false;
    if(!player.hasPermission("legendquests.edit")) return false;
    if(args.length == 0) {
      TextUtils.sendCenteredMessage(player,"&cUsage: /editquest <quest>");
      return false;
    }
    String questName = args[0];
    QuestBase quest = QuestManager.getQuestBaseByName(questName);
    if(quest == null) {
      TextUtils.sendCenteredMessage(player,"&cQuest not found!");
      return false;
    }
    quest.showGui(player);
    return false;
  }
}
