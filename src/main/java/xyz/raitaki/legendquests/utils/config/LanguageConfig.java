package xyz.raitaki.legendquests.utils.config;

import xyz.raitaki.legendquests.utils.TextUtils;

public class LanguageConfig extends ConfigManager {

  private static LanguageConfig instance;

  public LanguageConfig(String configName) {
    super(configName);
    instance = this;
  }

  /**
   * get a language text from the config
   *
   * @param path the path to the string
   * @return the string
   */
  public String get(String path, boolean colorize, String... replacements) {
    String text = getString(path);
    if (text.isEmpty()) {
      return path + " is empty, report this to an admin!";
    }
    return TextUtils.replaceStrings(text, colorize, replacements);
  }

  /**
   * @return the instance of the config
   */
  public static LanguageConfig getInstance() {
    return instance;
  }
}
