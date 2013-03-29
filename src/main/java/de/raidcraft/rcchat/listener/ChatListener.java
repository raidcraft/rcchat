package de.raidcraft.rcchat.listener;

import de.raidcraft.rcchat.channel.Channel;
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
        Channel channel = PlayerManager.INST.getMainChannel(event.getPlayer());
        if(channel == null) {
            return;
        }
        String prefix = channel.getPrefix();
        if(prefix.length() > 0) {
            prefix += " ";
        }
        message = prefix + message;

        channel.sendMessage(message);
        event.setCancelled(true);
    }

}
