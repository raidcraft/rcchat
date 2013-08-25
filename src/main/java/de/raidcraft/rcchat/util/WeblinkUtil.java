package de.raidcraft.rcchat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Philip Urban
 */
public class WeblinkUtil {

    private static Pattern pattern;

    private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
            + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    {
        pattern = Pattern.compile(IPADDRESS_PATTERN);
    }

    public static String obfuscateWeblinks(String text) {

        String newText = "";
        for(String word : text.split(" ")) {

            if(isWeblink(word) || isIP(word)) {
                word = "**********";
            }
            newText += word + " ";
        }
        return newText;
    }

    public static boolean hasWeblink(String text) {

        for(String word : text.split(" ")) {

            if(isWeblink(word)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isWeblink(String word) {

        word = word.trim();
        if(word.length() < 6) return false;

        if(word.endsWith(".") || word.startsWith(".")) {
            return false;
        }

        if(word.startsWith("http://") || word.startsWith("www.")) {
            return true;
        }

        int dotCount = word.split("\\.").length;
        if(dotCount > 0 && dotCount < 3) {
            return true;
        }

        return false;
    }

    public static boolean isIP(String word) {

        word = word.trim();
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }
}
