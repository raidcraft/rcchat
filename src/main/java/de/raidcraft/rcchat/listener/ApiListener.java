package de.raidcraft.rcchat.listener;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.events.PlayerChangeProfessionEvent;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.rcchat.tables.PlayersPrefixTable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * @author Philip Urban
 */
public class ApiListener implements Listener {

    @EventHandler
    public void PlayerChangeProfession(PlayerChangeProfessionEvent event) {

        RaidCraft.getTable(PlayersPrefixTable.class).removePrefix(event.getPlayer());
        ChatPlayerManager.INST.getPlayer(event.getPlayer()).setPrefix(null);
        ChatPlayerManager.INST.getPlayer(event.getPlayer()).setSuffix(null);
    }

}
