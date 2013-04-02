package de.raidcraft.rcchat.bungeecord;

import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class BungeeCordManager {

    public final static BungeeCordManager INST = new BungeeCordManager();
    public final static String MESSAGE_DELIMITER = "#@#";

    public void sendMessage(Player player, Channel channel, String message, MessageType type) {

        for(String world : channel.getWorlds()) {
            if(world.equalsIgnoreCase(player.getLocation().getWorld().getName())) continue;

            BungeeCordUtil.sendPluginMessage(player, world, RCChatPlugin.BUNGEECORD_CHANNEL, type.name() + MESSAGE_DELIMITER + message);
        }
    }

    public enum MessageType {
        CHAT_MESSAGE
    }
}
