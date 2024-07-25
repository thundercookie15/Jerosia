package nl.jerosia.utils;

import net.md_5.bungee.api.ChatColor;

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

    public static String applyHex(String message) {
        final Pattern hexPattern = Pattern.compile("&(#\\w{6})");
        Matcher matcher = hexPattern.matcher(ChatColor.translateAlternateColorCodes('&', message));
        StringBuilder builder = new StringBuilder();

        while (matcher.find())
            matcher.appendReplacement(builder, ChatColor.of(matcher.group(1)).toString());

        return matcher.appendTail(builder).toString();
    }
}
