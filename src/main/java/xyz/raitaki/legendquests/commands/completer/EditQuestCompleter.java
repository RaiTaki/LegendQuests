package xyz.raitaki.legendquests.commands.completer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import xyz.raitaki.legendquests.questhandlers.QuestBase;
import xyz.raitaki.legendquests.questhandlers.QuestManager;

public class EditQuestCompleter implements TabCompleter {

  @Override
  public List<String> onTabComplete(CommandSender sender, Command command, String alias,
      String[] args) {
    List<String> completions = new LinkedList<>();
    List<String> questNames = getQuestNames();
    if (!sender.hasPermission("legendquests.edit")) {
      return null;
    }
    if (args.length == 1) {
      StringUtil.copyPartialMatches(args[0], questNames, completions);
    }

    Collections.sort(completions);
    return completions;
  }

  public LinkedList<String> getQuestNames() {
    LinkedList<String> questNames = new LinkedList<>();
    for (QuestBase quest : QuestManager.getBaseQuests()) {
      questNames.add(quest.getName());
    }
    return questNames;
  }
}
