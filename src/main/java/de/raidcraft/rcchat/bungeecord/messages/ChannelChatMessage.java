package de.raidcraft.rcchat.bungeecord.messages;

import de.raidcraft.rcchat.channel.Channel;
import de.raidcraft.rcchat.channel.ChannelManager;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.bungeecord.messages.BungeeMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.MessageName;
import de.raidcraft.util.StringEncodingUtil;

/**
 * @author Philip
 */
@MessageName("CHANNEL_CHAT_MESSAGE")
public class ChannelChatMessage extends BungeeMessage {

    private String channelName;
    private String sender;
    private String message;

    public ChannelChatMessage(String channelName, String sender, String message) {

        this.channelName = channelName;
        this.sender = sender;
        this.message = message;
    }

    @Override
    protected String encode() {

        return channelName + BungeeManager.DELIMITER + sender + BungeeManager.DELIMITER + StringEncodingUtil.encode(message);
    }

    @Override
    public void process() {

        Channel channel = ChannelManager.INST.getChannel(channelName);
        if (channel != null) {
            channel.sendMessage(sender, StringEncodingUtil.decode(message));
        }
    }
}
