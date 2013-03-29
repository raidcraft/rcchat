package de.raidcraft.rcchat.listener;

import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Philip
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        String message = event.getMessage();
        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer(event.getPlayer());
        Channel channel = chatPlayer.getMainChannel();
        if(channel == null) {
            return;
        }
        String channelPrefix = channel.getPrefix();
        if(channelPrefix.length() > 0) {
            channelPrefix += " ";
        }

        String prefix = chatPlayer.getPrefix();
        String suffix = "";

        message = channelPrefix + prefix + message + suffix + ":";

        channel.sendMessage(message);
        event.setCancelled(true);
    }

}
