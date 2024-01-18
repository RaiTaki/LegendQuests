package xyz.raitaki.legendquests.commands.completer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

public class CreateQuestCompleter implements TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias,
      String[] args) {

    if (!sender.hasPermission("legendquests.edit")) {
      return null;
    }

    List<String> completions = new LinkedList<>();
    completions.add("Enter Quest Name");
    return completions;
  }
}
