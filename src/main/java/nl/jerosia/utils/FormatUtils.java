package nl.jerosia.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FormatUtils {

    public static List<String> parseColors(List<String> list) {
        return list.stream().map(FormatUtils::parseColors).collect(Collectors.toList());
    }

    public static String parseColors(String string) {
        string = applyHex(string);

        char[] b = string.toCharArray();

        IntStream.range(0, b.length).filter(i -> b[i] == '&' && "01234567890AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1).forEach(i -> {
            b[i] = 167;
            b[i + 1] = Character.toLowerCase(b[i + 1]);
        });
        return new String(b);
    }

    public static String parseEmoji(String message, String shortcut, String unicode) {
        return message.replace(shortcut, unicode);
    }

    public static String extractUrl(String text) {
        String regex = "((http://|https://)?(www.)?(([a-zA-Z0-9-]){2,}\\.){1,4}([a-zA-Z]){2,6}(/([a-zA-Z-_/.0-9#:?=&;,]*)?)?)\n";
        Pattern pattern = Pattern.compile(regex);

        if (text == null) return "";

        Matcher m = pattern.matcher(text);
        return m.matches() ? pattern.pattern() : "";
    }

    public static String applyHex(String message) {
        final Pattern hexPattern = Pattern.compile("&(#\\w{6})");
        Matcher matcher = hexPattern.matcher(ChatColor.translateAlternateColorCodes('&', message));
        StringBuilder builder = new StringBuilder();

        while (matcher.find())
            matcher.appendReplacement(builder, ChatColor.of(matcher.group(1)).toString());

        return matcher.appendTail(builder).toString();
    }

    public static String removeLastCharacter(String string) {
        if (string != null && !string.isEmpty()) return string.substring(0, string.length() - 1);

        return null;
    }

    public static String toMessage(String[] args) {
        StringBuilder out = new StringBuilder();

        for (String arg : args) out.append(arg).append(" ");

        return out.toString().trim();
    }

    public static double roundTwoDecimals(double toRound) {
        int rounded = (int) (toRound * 100);
        return (double) rounded / 100;
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String parsePlaceholders(String string, FormatUtils.Placeholder... placeholders) {
        Validate.notNull(string, "input string must not be null");
        Validate.noNullElements(placeholders, "no placeholder must be null");

        for (Placeholder p : placeholders) string = p.parse(string);

        return string;
    }

    public static List<String> parsePlaceholders(List<String> strings, FormatUtils.Placeholder... placeholders) {
        Validate.noNullElements(strings, "placeholder input strings must not be null");
        Validate.noNullElements(placeholders, "no placeholder must be null");

        for (Placeholder p : placeholders) strings = p.parse(strings);

        return strings;
    }

    public static String parsePapiPlaceholders(Player player, String string, FormatUtils.Placeholder... placeholders) {
        Validate.notNull(player, "Player must not be null");
        Validate.notNull(string, "Input string must not be null");
        Validate.noNullElements(placeholders, "Provided strings must not be null");

        for (Placeholder p : placeholders) string = p.parse(string);

        string = parsePapiPlaceholders(player, string);
        return string;
    }

    public static List<String> parsePapiPlaceholders(Player player, List<String> strings, FormatUtils.Placeholder... placeholders) {
        Validate.notNull(player, "Player must not be null");
        Validate.noNullElements(strings, "Input strings must not be null");
        Validate.noNullElements(placeholders, "Provided strings must not be null");

        for (Placeholder p : placeholders) strings = p.parse(strings);

        strings = parsePapiPlaceholders(player, strings);
        return strings;
    }

    public static String centerText(String text, int lineLength) {
        char[] chars = text.toCharArray();
        boolean isBold = false;
        double length = 0;
        ChatColor pholder;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '&' && chars.length != (i + 1) && (pholder = ChatColor.getByChar(chars[i + 1])) != null) {
                if (pholder != ChatColor.UNDERLINE && pholder != ChatColor.ITALIC
                        && pholder != ChatColor.STRIKETHROUGH && pholder != ChatColor.MAGIC) {
                    isBold = chars[i + 1] == 'l'; // true if the next is a bold modifier
                }
                i++;
            } else {
                length += 1;
                if (isBold) length += 0.1555555555555556;
            }
        }

        double spaces = (lineLength - length) / 2;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < spaces; i++) builder.append(' ');
        String copy = builder.toString(); // avoid insertions, it's kinda costly I believe
        builder.append(text).append(copy);

        return builder.toString();
    }

    public static String parsePapiPlaceholders(Player player, String string) {
        Validate.notNull(player, "Player must not be null");
        Validate.notNull(string, "Provided string must not be null");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return string;

        try {
            return (String) Class.forName("me.clip.placeholderapi.PlaceholderAPI").getMethod("setPlaceholders", Player.class, String.class).invoke(null, player, string);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return string;
        }
    }

    public static List<String> parsePapiPlaceholders(Player player, List<String> string) {
        Validate.notNull(player, "Player must not be null");
        Validate.noNullElements(string, "Provided strings must not be null");
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) return string;
        try {
            return (List<String>) Class.forName("me.clip.placeholderapi.PlaceholderAPI").getMethod("setPlaceholders", Player.class, List.class).invoke(null, player, string);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return string;
        }
    }

    public static String parsePlaceholders(Player player, String string) {
        return PlaceholderAPI.setPlaceholders(player, string);
    }

    public static List<String> parsePlaceholders(Player player, List<String> text) {
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public static List<String> replaceInStringList(List<String> list, Object[] before, Object[] after) {
        if (before.length != after.length)
            throw new IllegalArgumentException("before[] length must be equal to after[] length");
        List<String> newList = new ArrayList<>();

        for (String string : list) {
            for (int i = 0; i < before.length; ++i) string = string.replace(before[i].toString(), after[i].toString());

            newList.add(string);
        }

        return newList;
    }

    public static String capFirst(String s) {
        if (s.isEmpty()) return "";

        String[] words = s.split(" ");
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            builder.append(word.toUpperCase().charAt(0)).append(word.substring(1).toLowerCase());
            if (i + 1 < words.length) builder.append(" ");
        }
        return builder.toString();
    }

    public static class Placeholder {
        private final String key;
        private final String value;

        public Placeholder(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String parse(String string) {
            return string.replace(this.key, this.value);
        }

        public List<String> parse(List<String> list) {
            return list.stream().map(this::parse).collect(Collectors.toList());
        }
    }
}
