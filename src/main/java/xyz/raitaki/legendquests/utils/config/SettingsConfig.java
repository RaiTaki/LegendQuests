package xyz.raitaki.legendquests.utils.config;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class SettingsConfig extends ConfigManager {

  private static SettingsConfig instance;

  public SettingsConfig(String configName) {
    super(configName);
    instance = this;
  }

  /**
   * @return the instance of the config
   */
  public static SettingsConfig getInstance() {
    return instance;
  }

  /**
   * get a location from the config
   *
   * @param locationPath the path to the location
   * @return the location
   */
  public @Nullable Location getLocationValue(String locationPath) {
    return (Location) get(locationPath);
  }

  /**
   * set a location in the config
   *
   * @param locationPath the path to the location
   * @param location     the location to set
   */
  public void setLocation(String locationPath, Location location) {
    instance.set(locationPath, location);
  }
}
