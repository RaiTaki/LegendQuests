package xyz.raitaki.legendquests;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.raitaki.legendquests.commands.CreateQuestCommand;
import xyz.raitaki.legendquests.commands.EditQuestCommand;
import xyz.raitaki.legendquests.commands.QuestInfoCommand;
import xyz.raitaki.legendquests.commands.completer.CreateQuestCompleter;
import xyz.raitaki.legendquests.commands.completer.EditQuestCompleter;
import xyz.raitaki.legendquests.commands.completer.QuestInfoCompleter;
import xyz.raitaki.legendquests.database.DatabaseConnection;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.placeholders.CheckpointPlaceholder;
import xyz.raitaki.legendquests.utils.EconomyUtils;
import xyz.raitaki.legendquests.utils.PacketDisplay;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public final class LegendQuests extends JavaPlugin {

  private static LegendQuests instance;

  @Override
  public void onEnable() {
    instance = this;
    EconomyUtils.setupEconomy();
    registerCommands();

    new LanguageConfig("language.yml");
    new SettingsConfig("settings.yml");

    DatabaseConnection.connect();
    DatabaseConnection.loadAllQuests();
    QuestManager.registerEvents();
    CheckpointPlaceholder.registerPlaceholder();
    PacketDisplay.startUpdateTimer();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    DatabaseConnection.saveAllPlayersSynced();
    DatabaseConnection.close();
  }

  /**
   * @return the instance of the plugin
   */
  public static LegendQuests getInstance() {
    return instance;
  }

  /**
   * register the commands
   */
  public void registerCommands() {
    getCommand("editquest").setExecutor(new EditQuestCommand());
    getCommand("editquest").setTabCompleter(new EditQuestCompleter());

    getCommand("createquest").setExecutor(new CreateQuestCommand());
    getCommand("createquest").setTabCompleter(new CreateQuestCompleter());

    getCommand("questinfo").setExecutor(new QuestInfoCommand());
    getCommand("questinfo").setTabCompleter(new QuestInfoCompleter());
  }
}
