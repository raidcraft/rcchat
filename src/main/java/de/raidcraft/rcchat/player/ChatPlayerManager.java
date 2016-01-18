package de.raidcraft.rcchat.player;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcchat.tables.TPlayersChannel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Philip
 */
public class ChatPlayerManager {

    public final static ChatPlayerManager INST = new ChatPlayerManager();

    private Map<String, ChatPlayer> players = new HashMap<>();

    public void reload() {

        players.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayer(player);
        }
    }

    public void loadPlayer(Player player) {

        ChatPlayer chatPlayer = new ChatPlayer(player);
        players.put(player.getName(), chatPlayer);

        Channel mainChannel = null;
        List<Channel> playersChannel = new ArrayList<>();
        List<ChannelAssignment> channels = TPlayersChannel.getChannels(player);
        for (ChannelAssignment assignment : channels) {

            Channel channel = ChannelManager.INST.getChannel(assignment.getChannel());
            if (channel == null) continue;
            playersChannel.add(channel);
            if (assignment.getType() == AssignmentType.MAIN) {
                mainChannel = channel;
            }
        }
        for (Channel channel : playersChannel) {
            channel.join(player);
        }
        if (mainChannel != null) {
            mainChannel.join(player);
        } else {
            Channel defaultChannel = ChannelManager.INST.getDefaultChannel();
            if (defaultChannel == null) {
                return;
            }
            defaultChannel.join(chatPlayer);
        }
    }

    public void unloadPlayer(Player player) {

        for (Channel channel : ChannelManager.INST.getChannels()) {
            channel.logout(player);
        }
        players.remove(player.getName());
    }

    public ChatPlayer getPlayer(Player player) {

        return players.get(player.getName());
    }

    public List<ChatPlayer> getPlayers() {

        List<ChatPlayer> playerList = new ArrayList<>();
        for (Map.Entry<String, ChatPlayer> entry : players.entrySet()) {
            playerList.add(entry.getValue());
        }
        return playerList;
    }
}
