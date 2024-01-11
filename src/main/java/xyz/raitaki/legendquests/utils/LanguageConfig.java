package xyz.raitaki.legendquests.utils;

public class LanguageConfig extends ConfigManager {

    private static LanguageConfig instance;

    public LanguageConfig(String configName) {
        super(configName);
        instance = this;
    }

    public String get(String path, boolean colorize, String... replacements) {
        String text = getString(path);
        if(text.isEmpty()){
            return path + " is empty, report this to an admin!";
        }
        return TextUtils.replaceStrings(text, colorize, replacements);
    }

    public static LanguageConfig getInstance() {
        return instance;
    }
}
