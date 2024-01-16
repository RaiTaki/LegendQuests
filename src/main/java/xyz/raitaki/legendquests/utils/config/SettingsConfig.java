package xyz.raitaki.legendquests.utils.config;

import org.bukkit.Location;
import org.jetbrains.annotations.Nullable;

public class SettingsConfig extends ConfigManager {

  private static SettingsConfig instance;

  public SettingsConfig(String configName) {
    super(configName);
    instance = this;
  }

  public static SettingsConfig getInstance() {
    return instance;
  }

  public @Nullable Location getLocationValue(String location) {
    return (Location) get(location);
  }

  public void setLocation(String locationPath, Location location) {
    instance.set(locationPath, location);
  }
}
