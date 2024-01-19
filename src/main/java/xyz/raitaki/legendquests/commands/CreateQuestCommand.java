package xyz.raitaki.legendquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.utils.TextUtils;

public class CreateQuestCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!sender.hasPermission("legendquests.createquest")) {
      return false;
    }

    if (args.length == 0) {
      sender.sendMessage("§cUsage: /createquest <name>");
      return false;
    }

    String name = args[0];
    new QuestBase(name);

    sender.sendMessage(TextUtils.replaceColors("&aQuest created!"));
    return false;
  }
}
