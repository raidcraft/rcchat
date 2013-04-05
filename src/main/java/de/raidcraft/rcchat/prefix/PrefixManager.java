package de.raidcraft.rcchat.prefix;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.tables.PlayerPrefixTable;
import de.raidcraft.rcchat.tables.PlayersPrefixTable;
import de.raidcraft.rcchat.tables.WorldPrefixTable;
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

    private Map<Integer, PlayerPrefix> playerPrefixes = new HashMap<>();
    private Map<String, WorldPrefix> worldPrefixes = new HashMap<>();

    public void reload() {

        playerPrefixes.clear();
        List<PlayerPrefix> playerPrefixList = RaidCraft.getTable(PlayerPrefixTable.class).getPrefixes();
        for(PlayerPrefix playerPrefix : playerPrefixList) {
            playerPrefixes.put(playerPrefix.getId(), playerPrefix);
        }

        worldPrefixes.clear();
        List<WorldPrefix> worldPrefixList = RaidCraft.getTable(WorldPrefixTable.class).getPrefixes();
        for(WorldPrefix worldPrefix : worldPrefixList) {
            worldPrefixes.put(worldPrefix.getWorld().toLowerCase(), worldPrefix);
        }
    }

    public PlayerPrefix getPrefix(int id) {

        return playerPrefixes.get(id);
    }

    public String getPrefix(Player player) {

        PlayerPrefix savedPlayerPrefix = RaidCraft.getTable(PlayersPrefixTable.class).getPrefix(player);

        if(savedPlayerPrefix != null && player.hasPermission(savedPlayerPrefix.getPermission())) {
            return savedPlayerPrefix.getPrefix();
        }

        PlayerPrefix newPlayerPrefix = null;
        for(Map.Entry<Integer, PlayerPrefix> entry : playerPrefixes.entrySet()) {

            PlayerPrefix playerPrefix = entry.getValue();
            if(playerPrefix.hasPermission() && !player.hasPermission(playerPrefix.getPermission())) continue;

            if(newPlayerPrefix == null || playerPrefix.getPriority() > newPlayerPrefix.getPriority()) {
                newPlayerPrefix = playerPrefix;
            }
        }

        if(newPlayerPrefix == null) {
            return "[" + ChatColor.GRAY + "Gast" + ChatColor.WHITE + "]";
        }
        RaidCraft.getTable(PlayersPrefixTable.class).savePrefix(player, newPlayerPrefix);
        return newPlayerPrefix.getPrefix();
    }

    public String getWorldPrefix(String worldName) {
        worldName = worldName.toLowerCase();
        if(worldPrefixes.containsKey(worldName)) {
            return worldPrefixes.get(worldName).getPrefix();
        }
        return "";
    }
}
