package de.raidcraft.rcchat;

import org.bukkit.entity.Player;

/**
 * @author Philip
 */
public class ChatPlayer {

    Player player;

    public ChatPlayer(Player player) {

        this.player = player;
    }

    public Player getPlayer() {

        return player;
    }

    public boolean isMuted() {

        //TODO isMuted
    }

}
