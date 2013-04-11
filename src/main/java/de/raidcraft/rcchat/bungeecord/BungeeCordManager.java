package de.raidcraft.rcchat.bungeecord;

import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.util.BungeeCordUtil;
import de.raidcraft.util.StringEncodingUtil;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class BungeeCordManager {

    public final static BungeeCordManager INST = new BungeeCordManager();
    public final static String MESSAGE_DELIMITER = "#@#";

    public void sendMessage(Player player, Channel channel, String message, MessageType type) {

        message = StringEncodingUtil.encode(message);

        for(String world : channel.getWorlds()) {
            if(world.equalsIgnoreCase(player.getLocation().getWorld().getName())) continue;

            String decoded = type.name() + MESSAGE_DELIMITER + System.currentTimeMillis() + MESSAGE_DELIMITER + channel.getName() + MESSAGE_DELIMITER + message;
            BungeeCordUtil.sendPluginMessage(player, world, RCChatPlugin.BUNGEECORD_CHANNEL, decoded);
        }
    }

    public enum MessageType {
        CHAT_MESSAGE
    }
}
