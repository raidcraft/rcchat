package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Philip on 18.01.2016.
 */
@Getter
@Setter
@Entity
@Table(name = "rcchat_channels")
public class TChannel {

    @Id
    private int id;
    private String name;
    private String permission;
    private String prefix;
    private String color;
    private String aliases;
    private String type;

    public static List<Channel> getChannels() {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        List<Channel> channels = new ArrayList<>();

        List<TChannel> tChannels = plugin.getDatabase().find(TChannel.class).findList();
        for(TChannel tChannel : tChannels) {
            String[] aliases = tChannel.getAliases().split(",");

            Channel channel = new Channel(
                    tChannel.getName(),
                    tChannel.getPermission(),
                    tChannel.getPrefix(),
                    tChannel.getColor(),
                    aliases,
                    tChannel.getType(),
                    TChannelWorld.getWorlds(tChannel.getId())
            );
            channels.add(channel);
        }

        return channels;
    }
}
