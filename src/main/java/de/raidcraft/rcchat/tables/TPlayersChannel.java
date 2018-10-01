package de.raidcraft.rcchat.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.player.AssignmentType;
import de.raidcraft.rcchat.player.ChannelAssignment;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Philip on 18.01.2016.
 */
@Getter
@Setter
@Entity
@Table(name = "rc_chat_players_channel")
public class TPlayersChannel {

    @Id
    private int id;
    private String player;
    private UUID playerId;
    private String channel;
    private String type;

    public static List<ChannelAssignment> getChannels(Player player) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        List<ChannelAssignment> channels = new ArrayList<>();


        List<TPlayersChannel> tPlayersChannels =
                plugin.getRcDatabase().find(TPlayersChannel.class).where().eq("player_id", player.getUniqueId()).findList();
        if(tPlayersChannels != null) {
            for (TPlayersChannel tPlayersChannel : tPlayersChannels) {
                channels.add(new ChannelAssignment(tPlayersChannel.getChannel(), tPlayersChannel.getType()));
            }
        }
        return channels;
    }

    public static void addChannel(Player player, Channel channel) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);
        removeChannel(player, channel);
        removeMainTag(player);

        TPlayersChannel tPlayersChannel = new TPlayersChannel();
        tPlayersChannel.setPlayerId(player.getUniqueId());
        tPlayersChannel.setPlayer(player.getName());
        tPlayersChannel.setType(AssignmentType.MAIN.name());
        tPlayersChannel.setChannel(channel.getName());
        plugin.getRcDatabase().save(tPlayersChannel);
    }

    public static void removeChannel(Player player, Channel channel) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        List<TPlayersChannel> tPlayersChannels =
                plugin.getRcDatabase().find(TPlayersChannel.class).where()
                        .eq("player_id", player.getUniqueId()).eq("channel", channel.getName()).findList();
        if(tPlayersChannels != null) {
            for (TPlayersChannel tPlayersChannel : tPlayersChannels) {
                plugin.getRcDatabase().delete(tPlayersChannel);
            }
        }
    }

    public static void removeMainTag(Player player) {

        RCChatPlugin plugin = RaidCraft.getComponent(RCChatPlugin.class);

        List<TPlayersChannel> tPlayersChannels =
                plugin.getRcDatabase().find(TPlayersChannel.class).where()
                        .eq("player_id", player.getUniqueId()).eq("type", AssignmentType.MAIN.name()).findList();
        if(tPlayersChannels != null) {
            for (TPlayersChannel tPlayersChannel : tPlayersChannels) {
                tPlayersChannel.setType(AssignmentType.NORMAL.name());
                plugin.getRcDatabase().update(tPlayersChannel);
            }
        }
    }
}
