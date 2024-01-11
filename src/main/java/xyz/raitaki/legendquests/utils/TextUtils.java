package xyz.raitaki.legendquests.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;

public class TextUtils {

    public static String replaceStrings(String text, boolean colorize, String... replacements) {
        for (int i = 0; i < replacements.length; i += 2) {
            text = text.replace(replacements[i], replacements[i + 1]);
        }
        if(colorize) text = replaceColors(text);
        return text;
    }

    public static String replaceColors(String text) {
        return IridiumColorAPI.process(text);
    }
}
