package xyz.raitaki.legendquests.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import xyz.raitaki.legendquests.LegendQuests;

public abstract class ConfigManager {

    private YamlConfiguration config;
    private LegendQuests plugin;
    private String configName;
    public ConfigManager(String configName){
        plugin = LegendQuests.getInstance();
        plugin.saveResource(configName, false);
        this.configName = configName;
        config = YamlConfiguration.loadConfiguration(plugin.getDataFolder().toPath().resolve(configName).toFile());
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(plugin.getDataFolder().toPath().resolve(configName).toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(plugin.getDataFolder().toPath().resolve(configName).toFile());
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    public Object get(String path) {
        return config.get(path);
    }

    public String getString(String path) {
        String value = config.getString(path);
        if(value == null) {
            return "";
        }
        return config.getString(path);
    }

    public int getInt(String path) {
        int value = config.getInt(path);
        return config.getInt(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public long getLong(String path) {
        return config.getLong(path);
    }
}
