package de.raidcraft.rcchat.bungeecord.messages;

import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.bungeecord.messages.BungeeMessage;
import de.raidcraft.util.StringEncodingUtil;

/**
 * @author Philip
 */
public class ChannelChatMessage extends BungeeMessage {

    private String channelName;
    private String message;

    public ChannelChatMessage(String channelName, String message) {

        this.channelName = channelName;
        this.message = message;
    }

    @Override
    protected String encode() {

        return channelName + BungeeManager.DELIMITER + StringEncodingUtil.encode(message);
    }

    @Override
    public void process() {

        Channel channel = ChannelManager.INST.getChannel(channelName);
        if(channel != null) {
            channel.sendMessage(StringEncodingUtil.decode(message));
        }
    }
}
