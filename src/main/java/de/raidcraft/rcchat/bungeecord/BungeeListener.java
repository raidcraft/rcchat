package de.raidcraft.rcchat.bungeecord;

import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.util.BungeeCordUtil;
import de.raidcraft.util.SignUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * @author Philip
 */
public class BungeeListener implements PluginMessageListener {

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] encoded) {

        if(!channel.equals("BungeeCord")) return;

        String message = BungeeCordUtil.decodeMessage(encoded, RCChatPlugin.BUNGEECORD_CHANNEL);
        if(message == null) return;

        String[] parts = message.split(BungeeCordManager.MESSAGE_DELIMITER);

        BungeeCordManager.MessageType type = BungeeCordManager.MessageType.valueOf(parts[0]);
        String content = parts[1];
        content = SignUtil.parseColor(content);

        // broadcast incoming chat message
        if(type == BungeeCordManager.MessageType.CHAT_MESSAGE) {
            Bukkit.broadcastMessage(content);
            return;
        }
    }
}
