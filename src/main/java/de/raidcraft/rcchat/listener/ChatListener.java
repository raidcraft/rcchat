package de.raidcraft.rcchat.listener;

import de.raidcraft.api.chat.AutoCompletionProvider;
import de.raidcraft.api.chat.Chat;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.util.SignUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

/**
 * @author Philip
 */
public class ChatListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer(player);

        String message = event.getMessage();
        if (!player.hasPermission("rcchat.colorize")) {
            message = SignUtil.destroyColor(event.getMessage());
        }

        if (chatPlayer.hasPrivateChat()) {
            chatPlayer.sendMessageToPartner(message);
            event.setCancelled(true);
            return;
        }

        chatPlayer.sendMessage(message);
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTabComplete(PlayerChatTabCompleteEvent event) {

        Character token = null;
        if (event.getLastToken().length() > 0) {
            token = event.getLastToken().charAt(0);
        }
        if (!Chat.getAutoCompletionProviders().containsKey(token)) {
            return;
        }
        ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer(event.getPlayer());
        AutoCompletionProvider provider = Chat.getAutoCompletionProviders().get(token);
        String message = null;
        if (event.getLastToken().length() > 1) {
            message = event.getLastToken().substring(1);
        }
        if (provider.getMinLength() == 0 || (message != null && provider.getMinLength() <= message.length())) {
            event.getTabCompletions().addAll(provider.getAutoCompleteItems(event.getPlayer(), message));
        } else {
            chatPlayer.getPlayer().sendMessage(ChatColor.RED + provider.getErrorMessage());
        }
    }
}
