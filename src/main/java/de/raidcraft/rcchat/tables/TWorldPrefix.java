package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.prefix.WorldPrefix;
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
@Table(name = "rcchat_world_prefixes")
public class TWorldPrefix {

    @Id
    private int id;
    private String world;
    private String prefix;

    public static List<WorldPrefix> getPrefixes() {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        List<WorldPrefix> worldPrefixes = new ArrayList<>();

        List<TWorldPrefix> tWorldPrefixes = plugin.getRcDatabase().find(TWorldPrefix.class).findList();
        if(tWorldPrefixes != null) {
            for (TWorldPrefix tWorldPrefix : tWorldPrefixes) {
                worldPrefixes.add(new WorldPrefix(tWorldPrefix.getPrefix(), tWorldPrefix.getWorld()));
            }
        }

        return worldPrefixes;
    }
}
