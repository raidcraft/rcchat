package de.raidcraft.rcchat.listener;

import de.raidcraft.rcchat.bungeecord.BungeeCordManager;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import de.raidcraft.rcchat.prefix.PrefixManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Philip
 */
public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        String message = event.getMessage();
        Player player = event.getPlayer();
        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer(player);
        Channel channel = chatPlayer.getMainChannel();
        if(channel == null) {
            player.sendMessage(ChatColor.RED + "Du schreibst in keinem Channel");
            event.setCancelled(true);
            return;
        }
        String channelPrefix = channel.getPrefix();
        if(channelPrefix.length() > 0) {
            channelPrefix += " ";
        }

        String worldPrefix = PrefixManager.INST.getWorldPrefix(player.getLocation().getWorld().getName());
        String prefix = chatPlayer.getPrefix();
        String suffix = chatPlayer.getSuffix();
        String nameColor = chatPlayer.getNameColor();
        String channelColor = channel.getColor();

        message = worldPrefix + ChatColor.RESET + channelPrefix + ChatColor.RESET  + prefix + ChatColor.RESET + nameColor +
                player.getName() + ChatColor.RESET + suffix + ChatColor.RESET + ": " + channelColor + message;

        channel.sendMessage(message);
        BungeeCordManager.INST.sendMessage(player, channel, message, BungeeCordManager.MessageType.CHAT_MESSAGE);

        event.setCancelled(true);
    }

}
