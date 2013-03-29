package de.raidcraft.rcchat.prefix;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.tables.PlayersPrefixTable;
import de.raidcraft.rcchat.tables.PrefixTable;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class PrefixManager {

    public final static PrefixManager INST = new PrefixManager();

    private Map<Integer, Prefix> prefixes = new HashMap<>();

    public void reload() {

        prefixes.clear();
        List<Prefix> prefixList = RaidCraft.getTable(PrefixTable.class).getPrefixes();
        for(Prefix prefix : prefixList) {
            prefixes.put(prefix.getId(), prefix);
        }
    }

    public Prefix getPrefix(int id) {

        return prefixes.get(id);
    }

    public String getPrefix(Player player) {

        Prefix savedPrefix = RaidCraft.getTable(PlayersPrefixTable.class).getPrefix(player);

        if(savedPrefix != null && player.hasPermission(savedPrefix.getPermission())) {
            return savedPrefix.getPrefix();
        }

        Prefix newPrefix = null;
        for(Map.Entry<Integer, Prefix> entry : prefixes.entrySet()) {

            Prefix prefix = entry.getValue();
            if(prefix.hasPermission() && !player.hasPermission(prefix.getPermission())) continue;

            if(newPrefix == null || prefix.getPriority() > newPrefix.getPriority()) {
                newPrefix = prefix;
            }
        }

        if(newPrefix == null) {
            return "[" + ChatColor.GRAY + "Gast" + ChatColor.WHITE + "]";
        }
        RaidCraft.getTable(PlayersPrefixTable.class).savePrefix(player, newPrefix);
        return newPrefix.getPrefix();
    }
}
