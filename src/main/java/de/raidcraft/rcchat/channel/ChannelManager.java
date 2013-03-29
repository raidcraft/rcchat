package de.raidcraft.rcchat.channel;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.player.PlayerManager;
import de.raidcraft.rcchat.tables.ChannelsTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class ChannelManager {

    public final static ChannelManager INST = new ChannelManager();

    private Map<String, Channel> channels = new HashMap<>();
    private Channel defaultChannel;

    public void reload() {

        channels.clear();

        List<Channel> databaseChannels = RaidCraft.getTable(ChannelsTable.class).getChannels();

        for(Channel channel : databaseChannels) {
            registerChannel(channel);
            if(channel.getType() == ChannelType.DEFAULT) {
                defaultChannel = channel;
            }
            RaidCraft.LOGGER.info("[RCChat] Load channel '" + channel.getName() + "'");
        }

        for(Player player : Bukkit.getOnlinePlayers()) {
            PlayerManager.INST.loadPlayer(player);
        }
    }

    public void registerChannel(Channel channel) {

        channels.put(channel.getName().toLowerCase(), channel);
        for(String alias : channel.getAliases()) {
            channels.put(alias, channel);
        }
    }

    public Channel getDefaultChannel() {

        if(defaultChannel == null) {
            RaidCraft.LOGGER.warning("[RCChat] No default channel configured!");
            return null;
        }
        return defaultChannel;
    }

    public List<Channel> getChannels() {

        List<Channel> channelList = new ArrayList<>();
        for(Map.Entry<String, Channel> entry : channels.entrySet()) {
            channelList.add(entry.getValue());
        }
        return channelList;
    }

    public Channel getChannel(String name) {

        name = name.toLowerCase();
        return channels.get(name);
    }
}
