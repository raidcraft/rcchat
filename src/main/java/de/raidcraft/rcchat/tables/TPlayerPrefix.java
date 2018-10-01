package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.prefix.PlayerPrefix;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 18.01.2016.
 */
@Getter
@Setter
@Entity
@Table(name = "rc_chat_player_prefixes")
public class TPlayerPrefix {

    @Id
    private int id;
    private String prefix;
    private String permission;
    private int priority;

    public static List<PlayerPrefix> getPrefixes() {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        List<PlayerPrefix> playerPrefixes = new ArrayList<>();

        List<TPlayerPrefix> tPlayerPrefixes = plugin.getRcDatabase().find(TPlayerPrefix.class).findList();
        if(tPlayerPrefixes != null) {
            for (TPlayerPrefix tPlayerPrefix : tPlayerPrefixes) {
                PlayerPrefix playerPrefix = new PlayerPrefix(
                        tPlayerPrefix.getId(),
                        tPlayerPrefix.getPrefix(),
                        tPlayerPrefix.getPermission(),
                        tPlayerPrefix.getPriority()
                );
                playerPrefixes.add(playerPrefix);
            }
        }

        return playerPrefixes;
    }
}
