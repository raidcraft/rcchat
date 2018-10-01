package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import de.raidcraft.rcchat.prefix.PrefixManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

/**
 * Created by Philip on 18.01.2016.
 */
@Getter
@Setter
@Entity
@Table(name = "rc_chat_players_prefix")
public class TPlayersPrefix {

    @Id
    private int id;
    private String player;
    private UUID playerId;
    private int prefix;

    public static PlayerPrefix getPrefix(Player player) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        List<TPlayersPrefix> tPlayersPrefixes = plugin.getRcDatabase().find(TPlayersPrefix.class)
                .where().eq("player_id", player.getUniqueId()).findList();
        if(tPlayersPrefixes == null) {
            return null;
        }

        for(TPlayersPrefix tPlayersPrefix : tPlayersPrefixes) {
            return PrefixManager.INST.getPrefix(tPlayersPrefix.getPrefix());
        }

        return null;
    }


    public static void savePrefix(Player player, PlayerPrefix playerPrefix) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        removePrefix(player);

        TPlayersPrefix tPlayersPrefix = new TPlayersPrefix();
        tPlayersPrefix.setPlayer(player.getName());
        tPlayersPrefix.setPlayerId(player.getUniqueId());
        tPlayersPrefix.setPrefix(playerPrefix.getId());
        plugin.getRcDatabase().save(tPlayersPrefix);
    }

    public static void removePrefix(Player player) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        List<TPlayersPrefix> tPlayersPrefixes = plugin.getRcDatabase().find(TPlayersPrefix.class)
                .where().eq("player_id", player.getUniqueId()).findList();
        if(tPlayersPrefixes != null) {
            for(TPlayersPrefix tPlayersPrefix : tPlayersPrefixes) {
                plugin.getRcDatabase().delete(tPlayersPrefix);
            }
        }
    }
}
