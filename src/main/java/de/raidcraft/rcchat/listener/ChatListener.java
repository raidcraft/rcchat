package de.raidcraft.rcchat.listener;

import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.PlayerManager;
import de.raidcraft.util.SignUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * @author Philip
 */
public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        String message = SignUtil.parseColor(event.getMessage());
        Player player = event.getPlayer();
        ChatPlayer chatPlayer = PlayerManager.INST.getPlayer(player);

        if(chatPlayer.hasPrivateChat()) {
            chatPlayer.sendMessageToPartner(message);
            event.setCancelled(true);
            return;
        }

        chatPlayer.sendMessage(message);
        event.setCancelled(true);
    }

}
