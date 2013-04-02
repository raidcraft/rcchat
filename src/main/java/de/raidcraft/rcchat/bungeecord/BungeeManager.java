package de.raidcraft.rcchat.bungeecord;

import de.raidcraft.RaidCraft;
import de.raidcraft.rcchat.RCChatPlugin;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Philip
 */
public class BungeeManager {

    public final static BungeeManager INST = new BungeeManager();

    public void sendMessage(Player player, String channel, String message) {

        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            DataOutputStream msgData = new DataOutputStream(bao);
            msgData.writeUTF("Forward");
            msgData.writeUTF(portal.getNetwork());	// Server
            msgData.writeUTF(channel);			// Channel
            msgData.writeShort(message.length()); 	// Data Length
            msgData.writeBytes(message); 			// Data
            player.sendPluginMessage(RaidCraft.getComponent(RCChatPlugin.class), "BungeeCord", bao.toByteArray());
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
    }

}
