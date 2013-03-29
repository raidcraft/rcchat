package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.tables.PlayersTable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class PlayerManager {

    public final static PlayerManager INST = new PlayerManager();

    private Map<String, Channel> mainChannels = new HashMap<>();

    public void loadPlayer(Player player) {

        boolean hasMain = false;
        List<ChannelAssignment> channels = RaidCraft.getTable(PlayersTable.class).getChannels(player);
        for(ChannelAssignment assignment : channels) {

            Channel channel = ChannelManager.INST.getChannel(assignment.getChannel());
            channel.join(player);
            if(assignment.getType() == AssignmentType.MAIN) {
                mainChannels.put(player.getName(), channel);
                hasMain = true;
            }
        }

        if(!hasMain) {
            Channel defaultChannel = ChannelManager.INST.getDefaultChannel();
            defaultChannel.join(player);
            mainChannels.put(player.getName(), defaultChannel);
        }
    }

    public void unloadPlayer(Player player) {

        for(Channel channel : ChannelManager.INST.getChannels()) {
            channel.logout(player);
        }
    }

    public Channel getMainChannel(Player player) {

        if(mainChannels.containsKey(player.getName())) {
            return mainChannels.get(player.getName());
        }

        return null;
    }

    public void setMainChannel(Player player, Channel channel) {

        mainChannels.put(player.getName(), channel);
    }
}
