package xyz.raitaki.legendquests.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;

public class QuestInfoCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player player)) {
      return false;
    }

    QuestPlayer questPlayer = QuestManager.getQuestPlayerByPlayer(player);
    questPlayer.sendQuestInfoChat();

    return false;
  }
}
