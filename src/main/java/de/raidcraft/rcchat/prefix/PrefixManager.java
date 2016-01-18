package de.raidcraft.rcchat.prefix;

import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcchat.player.ChatPlayerManager;
import de.raidcraft.rcchat.tables.TPlayerPrefix;
import de.raidcraft.rcchat.tables.TPlayersPrefix;
import de.raidcraft.rcchat.tables.TWorldPrefix;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class PrefixManager {

    public final static PrefixManager INST = new PrefixManager();
    public final static PlayerPrefix GUEST_PREFIX = new PlayerPrefix(0, "&8[&7Gast&8]&f", null, -1);

    private Map<Integer, PlayerPrefix> playerPrefixes = new HashMap<>();
    private Map<String, WorldPrefix> worldPrefixes = new HashMap<>();

    public void reload() {

        playerPrefixes.clear();
        List<PlayerPrefix> playerPrefixList = TPlayerPrefix.getPrefixes();
        for (PlayerPrefix playerPrefix : playerPrefixList) {
            playerPrefixes.put(playerPrefix.getId(), playerPrefix);
        }

        worldPrefixes.clear();
        List<WorldPrefix> worldPrefixList = TWorldPrefix.getPrefixes();
        for (WorldPrefix worldPrefix : worldPrefixList) {
            worldPrefixes.put(worldPrefix.getWorld().toLowerCase(), worldPrefix);
        }
    }

    public PlayerPrefix getPrefix(int id) {

        return playerPrefixes.get(id);
    }

    public PlayerPrefix getPrefix(Player player) {

        PlayerPrefix savedPlayerPrefix = TPlayersPrefix.getPrefix(player);

        if (savedPlayerPrefix != null && player.hasPermission(savedPlayerPrefix.getPermission())) {
            return savedPlayerPrefix;
        }

        PlayerPrefix newPlayerPrefix = null;
        for (Map.Entry<Integer, PlayerPrefix> entry : playerPrefixes.entrySet()) {

            PlayerPrefix playerPrefix = entry.getValue();
            if (playerPrefix.hasPermission() && !player.hasPermission(playerPrefix.getPermission())) continue;

            if (newPlayerPrefix == null || playerPrefix.getPriority() > newPlayerPrefix.getPriority()) {
                newPlayerPrefix = playerPrefix;
            }
        }

        if (newPlayerPrefix == null) {
            return null;
        }
        TPlayersPrefix.savePrefix(player, newPlayerPrefix);
        return newPlayerPrefix;
    }

    public List<PlayerPrefix> getPossiblePrefixes(Player player) {

        List<PlayerPrefix> possiblePrefixList = new ArrayList<>();
        for (Map.Entry<Integer, PlayerPrefix> entry : playerPrefixes.entrySet()) {

            PlayerPrefix prefix = entry.getValue();
            if (prefix.hasPermission() && !player.hasPermission(prefix.getPermission())) continue;
            possiblePrefixList.add(entry.getValue());
        }
        return possiblePrefixList;
    }

    public void setPlayerPrefix(Player player, PlayerPrefix prefix) {

        TPlayersPrefix.savePrefix(player, prefix);
        ChatPlayer chatPlayer = ChatPlayerManager.INST.getPlayer(player);
        chatPlayer.setPrefix(prefix);
        chatPlayer.setSuffix(null); // delete old prefix
    }

    public String getWorldPrefix(String worldName) {
        worldName = worldName.toLowerCase();
        if (worldPrefixes.containsKey(worldName)) {
            return worldPrefixes.get(worldName).getPrefix();
        }
        return "";
    }
}
