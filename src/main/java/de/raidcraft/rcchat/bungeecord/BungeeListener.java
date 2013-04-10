package de.raidcraft.rcchat.bungeecord;

import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.util.StringEncoding;
import de.raidcraft.util.BungeeCordUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author Philip
 */
public class BungeeListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String messageChannel, Player player, byte[] encoded) {

        if(!messageChannel.equals("BungeeCord")) return;

        String message = BungeeCordUtil.decodeMessage(encoded, RCChatPlugin.BUNGEECORD_CHANNEL);
        if(message == null) return;

        String[] parts = message.split(BungeeCordManager.MESSAGE_DELIMITER);

        BungeeCordManager.MessageType type = BungeeCordManager.MessageType.valueOf(parts[0]);
        String timestampString = parts[1];
        String channelName = parts[2];
        String content = parts[3];

        long timestamp = Long.parseLong(timestampString);
        // check for timeout
        if(System.currentTimeMillis() - timestamp > 3*1000) {
            return;
        }

        content = StringEncoding.decode(content);

        // send incoming chat message
        if(type == BungeeCordManager.MessageType.CHAT_MESSAGE && Bukkit.getOnlinePlayers().length > 0) {
            Channel channel = ChannelManager.INST.getChannel(channelName);
            if(channel != null) {
                channel.sendMessage(content);
            }
            return;
        }
    }
}
