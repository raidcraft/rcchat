package de.raidcraft.rcchat.bungeecord.messages;

import de.raidcraft.rcchat.player.ChatPlayer;
import de.raidcraft.rcmultiworld.BungeeManager;
import de.raidcraft.rcmultiworld.bungeecord.messages.BungeeMessage;
import de.raidcraft.rcmultiworld.bungeecord.messages.MessageName;
import de.raidcraft.util.StringEncodingUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Philip
 */
@MessageName("PRIVATE_CHAT_MESSAGE")
public class PrivateChatMessage extends BungeeMessage {

    private String sender;
    private String recipient;
    private String message;

    public PrivateChatMessage(String sender, String recipient, String message) {

        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    protected String encode() {

        return sender + BungeeManager.DELIMITER + recipient + BungeeManager.DELIMITER + StringEncodingUtil.encode(message);
    }

    @Override
    public void process() {

        Player player = Bukkit.getPlayer(recipient);
        if(player == null) return;

        ChatPlayer.sendPrivateMessage(player, sender, StringEncodingUtil.decode(message));
    }
}
