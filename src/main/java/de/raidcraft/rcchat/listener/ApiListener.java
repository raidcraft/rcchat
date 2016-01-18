package de.raidcraft.rcchat.listener;

import de.raidcraft.api.events.RCPlayerChangedProfessionEvent;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.rcchat.tables.TPlayersPrefix;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Philip Urban
 */
public class ApiListener implements Listener {

    @EventHandler
    public void PlayerChangeProfession(RCPlayerChangedProfessionEvent event) {

        TPlayersPrefix.removePrefix(event.getPlayer());
        ChatPlayerManager.INST.getPlayer(event.getPlayer()).setPrefix(null);
        ChatPlayerManager.INST.getPlayer(event.getPlayer()).setSuffix(null);
    }

}
