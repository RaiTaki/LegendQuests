package xyz.raitaki.legendquests.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;

public class TextUtils {

  private final static int CENTER_PX = 154;

  public static String replaceStrings(String text, boolean colorize, String... replacements) {
    for (int i = 0; i < replacements.length; i += 2) {
      text = text.replace(replacements[i], replacements[i + 1]);
    }
    if (colorize) {
      text = replaceColors(text);
    }
    return text;
  }

  public static String replaceColors(String text) {
    return IridiumColorAPI.process(text);
  }

  //https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/page-2
  public static void sendCenteredMessage(Player player, String message) {
    if (message == null || message.isEmpty()) {
      player.sendMessage("");
    }
    message = replaceColors(message);

    int messagePxSize = 0;
    boolean previousCode = false;
    boolean isBold = false;

    for (char c : message.toCharArray()) {
      if (c == '\u00a7') {
        previousCode = true;
        continue;
      } else if (previousCode) {
        previousCode = false;
        if (c == 'l' || c == 'L') {
          isBold = true;
          continue;
        } else {
          isBold = false;
        }
      } else {
        DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
        messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
        messagePxSize++;
      }
    }

    int halvedMessageSize = messagePxSize / 2;
    int toCompensate = CENTER_PX - halvedMessageSize;
    int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
    int compensated = 0;
    StringBuilder sb = new StringBuilder();
    while (compensated < toCompensate) {
      sb.append(" ");
      compensated += spaceLength;
    }
    player.sendMessage(sb + message);
  }

  public static String formatDateTime(long durationMillis) {
    long years = TimeUnit.MILLISECONDS.toDays(durationMillis) / 365;
    long days = TimeUnit.MILLISECONDS.toDays(durationMillis);
    long hours = TimeUnit.MILLISECONDS.toHours(durationMillis);
    long minutes = TimeUnit.MILLISECONDS.toMinutes(durationMillis) % 60;
    long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis) % 60;

    LanguageConfig languageConfig = LanguageConfig.getInstance();
    String yearText =
        years == 1 ? languageConfig.getString("date.year") : languageConfig.getString("date.years");
    String dayText =
        days == 1 ? languageConfig.getString("date.day") : languageConfig.getString("date.years");
    String hourText =
        hours == 1 ? languageConfig.getString("date.hour") : languageConfig.getString("date.hours");
    String minuteText = minutes == 1 ? languageConfig.getString("date.minute")
        : languageConfig.getString("date.minutes");
    String secondText = seconds == 1 ? languageConfig.getString("date.second")
        : languageConfig.getString("date.seconds");

    if (years > 0) {
      return String.format("%02d %s %02d %s %02d %s", years, yearText, days, dayText, hours,
          hourText);
    } else if (days > 0) {
      return String.format("%02d %s %02d %s %02d %s", days, dayText, hours, hourText, minutes,
          minuteText);
    } else if (hours > 0) {
      return String.format("%02d %s %02d %s %02d %s", hours, hourText, minutes, minuteText, seconds,
          secondText);
    } else if (minutes > 0) {
      return String.format("%02d %s %02d %s", minutes, minuteText, seconds, secondText);
    } else {
      return String.format("%02d %s", seconds, secondText);
    }
  }
}
