package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.tables.PlayersChannelTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class PlayerManager {

    public final static PlayerManager INST = new PlayerManager();

    private Map<String, ChatPlayer> players = new HashMap<>();

    public void reload() {

        players.clear();
        for(Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
    }

    public void loadPlayer(Player player) {

        ChatPlayer chatPlayer = new ChatPlayer(player);
        players.put(player.getName(), chatPlayer);

        boolean hasMain = false;
        List<ChannelAssignment> channels = RaidCraft.getTable(PlayersChannelTable.class).getChannels(player);
        for(ChannelAssignment assignment : channels) {

            Channel channel = ChannelManager.INST.getChannel(assignment.getChannel());
            channel.join(chatPlayer);
            if(assignment.getType() == AssignmentType.MAIN) {
                chatPlayer.setMainChannel(channel);
                hasMain = true;
            }
        }

        if(!hasMain) {
            Channel defaultChannel = ChannelManager.INST.getDefaultChannel();
            defaultChannel.join(chatPlayer);
        }
    }

    public void unloadPlayer(Player player) {

        for(Channel channel : ChannelManager.INST.getChannels()) {
            channel.logout(player);
        }
        players.remove(player.getName());
    }

    public ChatPlayer getPlayer(Player player) {

        return players.get(player.getName());
    }
}
