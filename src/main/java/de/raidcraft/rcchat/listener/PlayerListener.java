package de.raidcraft.rcchat.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.bungeecord.BungeeListener;
import de.raidcraft.rcchat.player.PlayerManager;
import org.bukkit.Bukkit;
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

        PlayerManager.INST.unloadPlayer(event.getPlayer());
        if(Bukkit.getOnlinePlayers().length == 1) {
            Bukkit.getMessenger().unregisterIncomingPluginChannel(RaidCraft.getComponent(RCChatPlugin.class));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        PlayerManager.INST.loadPlayer(event.getPlayer());
        if(Bukkit.getOnlinePlayers().length == 1) {
            Bukkit.getMessenger().registerIncomingPluginChannel(RaidCraft.getComponent(RCChatPlugin.class), "BungeeCord", new BungeeListener());
        }
    }
}
