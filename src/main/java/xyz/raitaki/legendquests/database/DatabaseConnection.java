package xyz.raitaki.legendquests.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import xyz.raitaki.legendquests.LegendQuests;
import xyz.raitaki.legendquests.database.objects.PlayerData;
import xyz.raitaki.legendquests.questhandlers.QuestManager;
import xyz.raitaki.legendquests.questhandlers.playerhandlers.QuestPlayer;
import xyz.raitaki.legendquests.utils.config.SettingsConfig;

public class DatabaseConnection {

  private static Connection connection;

  /**
   * Connect to the database
   */
  public static void connect() {
    try {
      Class.forName("org.mariadb.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      Bukkit.getLogger().warning("MariaDB JDBC Driver not found");
      Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
    }

    try {
      SettingsConfig config = SettingsConfig.getInstance();
      String host = config.getString("database.host");
      String port = config.getString("database.port");
      String database = config.getString("database.database");
      String username = config.getString("database.username");
      String password = config.getString("database.password");
      String url = "jdbc:mariadb://" + host + ":" + port + "/" + database;

      connection = DriverManager.getConnection(url, username, password);
      buildDatabase();
    } catch (SQLException exception) {
      Bukkit.getLogger().warning(exception.getMessage());
      Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
    }
  }

  private static void buildDatabase() {
    try {
      connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `players` ("
          + "`id` INT NOT NULL AUTO_INCREMENT,"
          + "`uuid` VARCHAR(36) NOT NULL,"
          + "`data` TEXT NOT NULL,"
          + "PRIMARY KEY (`id`),"
          + "UNIQUE INDEX `uuid_UNIQUE` (`uuid` ASC))");
    } catch (SQLException exception) {
      Bukkit.getLogger().warning(exception.getMessage());
      Bukkit.getPluginManager().disablePlugin(LegendQuests.getInstance());
    }
  }

  /**
   * Get the connection
   *
   * @return the connection
   */
  public static Connection getConnection() {
    return connection;
  }

  /**
   * Get the player data from the database
   *
   * @param uuid the uuid of the player
   * @return the player data
   */
  public static CompletableFuture<PlayerData> getPlayerData(String uuid) {
    CompletableFuture<PlayerData> future = new CompletableFuture<>();
    LegendQuests instance = LegendQuests.getInstance();

    instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
      String query = "SELECT * FROM players WHERE uuid = ?";
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, uuid);

        try (ResultSet resultSet = statement.executeQuery()) {
          if (!resultSet.next()) {
            insertPlayerDataToDatabase(QuestManager.getQuestPlayerByUUID(uuid));
            future.complete(null);
            return;
          }

          int id = resultSet.getInt("id");
          String pUUID = resultSet.getString("uuid");
          String data = resultSet.getString("data");
          JSONParser parser = new JSONParser();
          future.complete(new PlayerData(id, pUUID, (JSONArray) parser.parse(data)));
        }
      } catch (SQLException | ParseException e) {
        future.completeExceptionally(e);
        Bukkit.getLogger().warning(e.getMessage());
      }
    });

    return future;
  }

  /**
   * Save the player data to the database
   *
   * @param player the player to save
   */
  public static void savePlayerData(QuestPlayer player) {
    String query = "UPDATE players SET data = ? WHERE uuid = ?";
    LegendQuests instance = LegendQuests.getInstance();

    instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, player.getAsJSON().toJSONString());
        statement.setString(2, player.getUuid().toString());
        statement.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * Save the player data to the database synchronously
   */
  public static void savePlayerDataSync(QuestPlayer player) {
    String query = "UPDATE players SET data = ? WHERE uuid = ?";
    try (PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, player.getAsJSON().toJSONString());
      statement.setString(2, player.getUuid().toString());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Insert the player data to the database
   *
   * @param player the player to save
   */
  public static void insertPlayerDataToDatabase(QuestPlayer player) {
    String query = "INSERT INTO players (uuid, data) VALUES (?, ?)";
    LegendQuests instance = LegendQuests.getInstance();
    if(player.getQuests().isEmpty()){
      QuestManager.addBaseQuestToPlayer(player.getPlayer(), QuestManager.getBaseQuests().get(0));
    }
    instance.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
      try (PreparedStatement statement = connection.prepareStatement(query)) {
        statement.setString(1, player.getUuid().toString());
        statement.setString(2, player.getAsJSON().toJSONString());
        statement.executeUpdate();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
    });
  }

  /**
   * Close the connection
   */
  public static void close() {
    try {
      connection.close();
    } catch (SQLException e) {
      Bukkit.getLogger().warning(e.getMessage());
    }
  }

  /**
   * Save all players to the database synchronously
   */
  public static void saveAllPlayersSynced(){
    QuestManager.getQuestPlayers().values().forEach(player -> {
      if(player.getPlayer().isOnline()) {
        savePlayerDataSync(player);
      }
    });
  }

}
