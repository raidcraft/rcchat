package de.raidcraft.rcchat.namecolor;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class NameColorManager {

    public static String getColor(Player player) {

        NameColor newNameColor = null;
        for(NameColor nameColor : NameColor.values()) {

            if(!player.hasPermission("rcchat.namecolor." + nameColor.getColor().name().toLowerCase())) continue;

            if(newNameColor == null || newNameColor.getPriority() < nameColor.getPriority()) {
                newNameColor = nameColor;
            }

        }

        if(newNameColor == null) {
            return "";
        }
        return newNameColor.getColor().toString();
    }

    public static enum NameColor {

        DARK_RED(ChatColor.DARK_RED, 20),
        RED(ChatColor.RED, 19),
        DARK_BLUE(ChatColor.DARK_BLUE, 18),
        BLUE(ChatColor.BLUE, 17),
        AQUA(ChatColor.AQUA, 16),
        GOLD(ChatColor.GOLD, 15);

        private int priority;
        private ChatColor color;

        private NameColor(ChatColor color, int priority) {

            this.color = color;
            this.priority = priority;
        }

        public int getPriority() {

            return priority;
        }

        public ChatColor getColor() {

            return color;
        }
    }
}
