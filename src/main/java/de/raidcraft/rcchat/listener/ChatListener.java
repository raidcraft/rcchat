package de.raidcraft.rcchat.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.items.CustomItemStack;
import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.util.SignUtil;
import mkremins.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import java.util.stream.Collectors;

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
    public void sendItemText(InventoryClickEvent event) {

        if (event.getClick() == ClickType.MIDDLE && event.getWhoClicked() instanceof Player) {
            CustomItemStack customItem = RaidCraft.getCustomItem(event.getCurrentItem());
            if (customItem != null) {
                new FancyMessage("Nutze während dem Chatten ? [Tab] um alle Items die du " +
                        "mit Mittelklick angeklickt hast zu vervollständigen. Folgendes Item wurde hinzugefügt: ")
                        .color(ChatColor.YELLOW)
                        .then("[" + customItem.getItem().getName() + "]")
                        .color(customItem.getItem().getQuality().getColor())
                        .itemTooltip(customItem)
                        .send((Player) event.getWhoClicked());
                ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer((Player) event.getWhoClicked());
                chatPlayer.addAutoCompleteItem(customItem);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onTabComplete(PlayerChatTabCompleteEvent event) {

        ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer(event.getPlayer());
        if (event.getLastToken().startsWith("?")) {
            String token;
            if (event.getLastToken().length() > 1) {
                token = event.getLastToken().substring(1).toLowerCase();
            } else {
                token = null;
            }
            event.getTabCompletions().addAll(chatPlayer.getAutocompleteItems().stream()
                    .filter(i -> token == null || i.getItem().getName().toLowerCase().startsWith(token))
                    .map(i -> "?\"" + i.getItem().getName() + "\"")
                    .collect(Collectors.toList()));
        }
    }
}
