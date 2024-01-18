package xyz.raitaki.legendquests.utils;

import com.iridium.iridiumcolorapi.IridiumColorAPI;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import xyz.raitaki.legendquests.utils.config.LanguageConfig;

public class TextUtils {

  private final static int CENTER_PX = 154;

  /**
   * replace strings in a string
   *
   * @param text         the text to replace in
   * @param colorize     if the text should be colorized
   * @param replacements the replacements, in the format of "to replace", "replacement"
   * @return the replaced string
   */
  public static String replaceStrings(String text, boolean colorize, String... replacements) {
    for (int i = 0; i < replacements.length; i += 2) {
      text = text.replace(replacements[i], replacements[i + 1]);
    }
    if (colorize) {
      text = replaceColors(text);
    }
    return text;
  }

  /**
   * replace colors in a string
   *
   * @param text the text to replace in
   * @return the replaced string
   */
  public static String replaceColors(String text) {
    return IridiumColorAPI.process(text);
  }

  //https://www.spigotmc.org/threads/free-code-sending-perfectly-centered-chat-message.95872/page-2
  /**
   * send a centered message to a player
   *
   * @param player  the player to send the message to
   * @param message the message to send
   */
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

  /**
   * format a duration in milliseconds to a string
   *
   * @param durationMillis the duration in milliseconds
   * @return the formatted string
   */
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

  /**
   * parse a string to time
   *
   * @param text the text to parse
   * @return the time in milliseconds
   */
  public static long parseStringToTime(String text){
    long time = 0;
    text = text.replace(",", "");
    String[] split = text.split(" ");

    for(String s : split){
      if(s.contains("Y")){
        time += Integer.parseInt(s.replace("Y", "")) * 31536000000L;
      }
      else if(s.contains("D")){
        time += Integer.parseInt(s.replace("D", "")) * 86400000L;
      }
      else if(s.contains("H")){
        time += Integer.parseInt(s.replace("H", "")) * 3600000L;
      }else if(s.contains("M")){
        time += Integer.parseInt(s.replace("M", "")) * 60000L;
      }else if(s.contains("S")){
        time += Integer.parseInt(s.replace("S", "")) * 1000L;
      }
    }
    return time;
  }
}
