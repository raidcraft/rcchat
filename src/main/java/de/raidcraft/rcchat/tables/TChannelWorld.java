package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
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
@Table(name = "rcchat_channel_worlds")
public class TChannelWorld {

    @Id
    private int id;
    private int channel;
    private String world;

    public static List<String> getWorlds(int channelId) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        List<String> worlds = new ArrayList<>();

        List<TChannelWorld> tChannelWorlds = plugin.getRcDatabase().find(TChannelWorld.class).where().eq("channel", channelId).findList();
        if(tChannelWorlds != null) {
            for (TChannelWorld tChannelWorld : tChannelWorlds) {
                worlds.add(tChannelWorld.getWorld());
            }
        }

        return worlds;
    }
}
