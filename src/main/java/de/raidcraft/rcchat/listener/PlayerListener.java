package de.raidcraft.rcchat.listener;

import de.raidcraft.rcchat.player.ChatPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author Philip
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        ChatPlayerManager.INST.unloadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        ChatPlayerManager.INST.loadPlayer(event.getPlayer());
    }
}
