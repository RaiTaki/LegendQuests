package xyz.raitaki.legendquests.utils.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.bukkit.configuration.file.YamlConfiguration;
import xyz.raitaki.legendquests.LegendQuests;

public abstract class ConfigManager {

  private YamlConfiguration config;
  private LegendQuests plugin;
  private String configName;

  public ConfigManager(String configName) {
    plugin = LegendQuests.getInstance();
    plugin.saveResource(configName, false);
    this.configName = configName;
    config = YamlConfiguration.loadConfiguration(
        plugin.getDataFolder().toPath().resolve(configName).toFile());
    saveConfig();
  }

  /**
   * @return the config
   */
  public YamlConfiguration getConfig() {
    return config;
  }

  /**
   * save the config
   */
  public void saveConfig() {
    try {
      config.save(plugin.getDataFolder().toPath().resolve(configName).toFile());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * reload the config
   */
  public void reloadConfig() {
    config = YamlConfiguration.loadConfiguration(
        plugin.getDataFolder().toPath().resolve(configName).toFile());
  }

  /**
   * set a value in the config
   *
   * @param path  the path to the value
   * @param value the value to set
   */
  public void set(String path, Object value) {
    config.set(path, value);
    saveConfig();
  }

  /**
   * get a value from the config
   *
   * @param path the path to the value
   * @return the value
   */
  public Object get(String path) {
    return config.get(path);
  }

  /**
   * get a string from the config
   *
   * @param path the path to the value
   * @return the string
   */
  public String getString(String path) {
    String value = config.getString(path);
    if (value == null) {
      return "";
    }
    return config.getString(path);
  }

  /**
   * get an int from the config
   *
   * @param path the path to the value
   * @return the int
   */
  public int getInt(String path) {
    int value = config.getInt(path);
    return config.getInt(path);
  }

  /**
   * get a double from the config
   *
   * @param path the path to the value
   * @return the double
   */
  public double getDouble(String path) {
    return config.getDouble(path);
  }

  /**
   * get a boolean from the config
   *
   * @param path the path to the value
   * @return the boolean
   */
  public boolean getBoolean(String path) {
    return config.getBoolean(path);
  }

  /**
   * get a long from the config
   *
   * @param path the path to the value
   * @return the long
   */
  public long getLong(String path) {
    return config.getLong(path);
  }
}
